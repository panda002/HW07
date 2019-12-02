package com.ownproj.homework07;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static androidx.constraintlayout.widget.Constraints.TAG;


public class CreateTripFragment extends Fragment {


    private OnFragmentInteractionListener mListener;
    private ImageView  addcoverpic;
    private EditText title;
    private String owner, coverpic;
    private EditText longitude, latitude, friend_list;
    public static final int PICK_IMAGE = 1;
    private Spinner spinner;
    private Button savetrip;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private Uri filePath = null;
    Trips trips = new Trips();
    ChatRoom  chatRoom = new ChatRoom();
    private StorageReference storageReference;
    private ArrayList<String> listWithoutDuplicates;
    private ProgressBar progressBar;

    public CreateTripFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_trip, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        title = getActivity().findViewById(R.id.Trip_title);
        latitude = getActivity().findViewById(R.id.Trip_latitude);
        longitude = getActivity().findViewById(R.id.Trip_longitude);
        spinner = getActivity().findViewById(R.id.spinner);
        addcoverpic = getActivity().findViewById(R.id.Trip_iv_tripphoto);
        savetrip = getActivity().findViewById(R.id.Trip_btn_Savetrip);
        friend_list = getActivity().findViewById(R.id.friend_list);
        progressBar = getActivity().findViewById(R.id.progressBart);
        progressBar.setVisibility(getView().INVISIBLE);
        storageReference = FirebaseStorage.getInstance().getReference();

        Picasso.get().load(R.drawable.sendpicmsg).into(addcoverpic);

        addcoverpic.setOnClickListener(v -> chooseImage());

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference subjectsRef = rootRef.collection("Users");

        ArrayList<String> friends = new ArrayList<>();
        ArrayList<String> friendslist = new ArrayList<>();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, friends);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        subjectsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String subject = document.getString("email");
                    if (subject != user.getEmail()) {
                        friends.add(subject);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    friendslist.add(spinner.getSelectedItem().toString());
                    HashSet<String> listToSet = new HashSet<String>(friendslist);
                    //Creating Arraylist without duplicate values
                    listWithoutDuplicates = new ArrayList<String>(listToSet);
                    friend_list.setText(listWithoutDuplicates.toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        savetrip.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            if (title.getText().toString().equals("")) {
                title.setError("Trip title is required");
            } else if (latitude.getText().toString().equals("")) {
                latitude.setError("Please enter latitude");
            } else if (longitude.getText().toString().equals("")) {
                longitude.setError("Please enter longitude");
            } else if (filePath != null) {
                StorageReference profilePicture = storageReference.child("coverpic/" + filePath);
                profilePicture.putFile(filePath).addOnFailureListener(e -> {

                }).addOnSuccessListener(taskSnapshot -> taskSnapshot
                        .getMetadata()
                        .getReference()
                        .getDownloadUrl()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {

                                if (task.getResult().toString().equals("") || task.getResult().toString() == null) {
                                } else {
                                    trips.setCoverpic(task.getResult().toString());
                                }
                                final String tripid = latitude.getText().toString() + "," + longitude.getText().toString();
                                trips.setTitle(title.getText().toString());
                                trips.setLatitude(latitude.getText().toString());
                                trips.setLongitude(longitude.getText().toString());
                                trips.setFriend(listWithoutDuplicates);
                                trips.setOwner(user.getEmail());
                                trips.setTripId(tripid);
                                final DocumentReference docRef = db.collection("Trips").document(tripid);

                                trips.setTripId(tripid);

                                docRef.set(trips).addOnCompleteListener(task12 -> {
                                    if (task12.isSuccessful()) {
                                        chatRoom.setFriends(listWithoutDuplicates);
                                        chatRoom.setTripId(tripid);
                                        chatRoom.setTripChat(new ArrayList<Chats>());

                                        DocumentReference tripchat = db.collection("ChatRoom").document(tripid);

                                        tripchat.set(chatRoom).addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(getContext(), "Trip Created Successfully", Toast.LENGTH_SHORT).show();
                                                getActivity().getSupportFragmentManager().beginTransaction()
                                                        .replace(R.id.container, new DisplayProfileFragment(), "tag_CreateTripFragment")
                                                        .addToBackStack(null)
                                                        .commit();
                                            }
                                        });
                                    }
                                });
                            }
                        }));
            }else{
                Toast.makeText(getActivity(), "Cover Pic is required", Toast.LENGTH_SHORT).show();
            }

        });
    }
            /*db.collection("Trips")
                    .document(tripid)
                    .set(trips)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            Log.d("TAG", "Trip document written with ID: " + task.getResult());
                        }
                    }).addOnFailureListener(e -> Log.d("TAG", "DocumentSnapshot written with ID: " +e));
            Toast.makeText(getContext(), " Trip "+title.getText().toString()+" created Successfully", Toast.LENGTH_SHORT).show();
            mListener.gotoDisplayFragment();
        });

    }*/

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Create Trip");
    }

    public void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            Log.d(TAG, "onActivityResult filePath: "+filePath);
            //Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
            Picasso.get().load(filePath).into(addcoverpic);
        }


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (OnFragmentInteractionListener) context;

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        void gotoDisplayFragment();
    }
}
