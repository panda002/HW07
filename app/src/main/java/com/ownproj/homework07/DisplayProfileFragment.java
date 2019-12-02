package com.ownproj.homework07;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class DisplayProfileFragment extends Fragment {

    private OnFragmentInteractionListener mListener;


    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    Profile profile = new Profile();
    private TextView fullname;
    private TextView gender;
    private ImageView profilepic;
    private Button btn_createTrip;
    private Button btn_findTrip;
    private Button btn_findFriends;
    private Button btn_editProfile, bt_logout;
    private FirebaseAuth mAuth;
    private String email;
    FirebaseStorage storage;
    private StorageReference storageReference;
    final Trips trip = new Trips();

    public DisplayProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_display_profile, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("TAG", "onViewCreated in DisplayProfile Fragment: ");

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        fullname = getActivity().findViewById(R.id.tv_fragDisplayName);
        gender = getActivity().findViewById(R.id.tv_fragDisplayGender);
        profilepic = getActivity().findViewById(R.id.imageFragDisplayProfile);
        btn_createTrip = getActivity().findViewById(R.id.btn_createtripDPF);
        btn_editProfile = getActivity().findViewById(R.id.btn_editprofileDPF);
        btn_findTrip = getActivity().findViewById(R.id.btn_findtripDPF);
        bt_logout = getActivity().findViewById(R.id.btn_logoutp);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        email = user.getEmail();

        storageReference = FirebaseStorage.getInstance().getReference();

        DocumentReference docRef = db.collection("Users").document(email);

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                Log.d("TAG","task "+task.getResult());
                if (document.exists()) {
                    profile.setFname(document.getData().get("fname").toString());
                    profile.setLname(document.getData().get("lname").toString());
                    profile.setGender(document.getData().get("gender").toString());
                    if(document.getData().get("profileimage") != null){
                        profile.setProfileimage(document.getData().get("profileimage").toString());
                    }
                    fullname.setText(profile.getFname()+" "+profile.getLname());
                    gender.setText(profile.getGender());
                    Picasso.get().load(profile.getProfileimage()).into(profilepic);
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Toast.makeText(getActivity(), "No Trips Found. Create One?", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
        btn_createTrip.setOnClickListener(view -> mListener.gotoCreatetrip());

        btn_editProfile.setOnClickListener(view -> mListener.gotoProfileBuilder());
        btn_findTrip.setOnClickListener(view -> getFragmentManager().beginTransaction()
                .replace(R.id.container, new HomePageFragment(email), "tag_FindTripFragment")
                .addToBackStack(null)
                .commit());

        bt_logout.setOnClickListener(v -> {
            mAuth.signOut();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new DisplayHomeFragment(), "LoginActivity")
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Profile of - "+email);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        //void onFragmentInteraction(Uri uri);
        void gotoCreatetrip();

        void gotoProfileBuilder();

        void gotoTripBuilder(Trips trip);

    }
}
