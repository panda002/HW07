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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import com.squareup.picasso.Picasso;

import static android.app.Activity.RESULT_OK;
import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateProfileFragment extends Fragment {

    private Button btn_save, btn_logout;
    private EditText et_firstName;
    private EditText et_lastname;
    private RadioGroup radio_gender;
    private Profile profile = new Profile();
    private String fname, lname, email;
    private String gender;
    private RadioButton rb_male;
    private RadioButton rb_female;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ImageView profilepic;
    private OnFragmentInteractionListener mListener;
    private TextView owneremail;
    private FirebaseAuth mAuth;
    public static final int PICK_IMAGE = 1;
    private Uri filePath = null;
    private ProgressBar progressBar;

    FirebaseStorage storage;
    private StorageReference storageReference;

    public CreateProfileFragment(){
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.create_fragment_profile, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btn_save = getView().findViewById(R.id.bt_save);

        et_firstName = getView().findViewById(R.id.et_firstname);
        et_lastname = getView().findViewById(R.id.et_lastname);
        radio_gender = getView().findViewById(R.id.radio_department);
        profilepic = getView().findViewById(R.id.iv_selectprofile);
        btn_logout = getView().findViewById(R.id.btn_logout);
        owneremail = getView().findViewById(R.id.user_email);
        rb_male = getView().findViewById(R.id.radioMale);
        rb_female = getView().findViewById(R.id.radioFmale);
        progressBar = getView().findViewById(R.id.progressBar2);
        //progressBar.setVisibility(View.INVISIBLE);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        email = user.getEmail();
        owneremail.setText(user.getEmail());

        storageReference = FirebaseStorage.getInstance().getReference();

        DocumentReference docRef = db.collection("Users").document(email);

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                progressBar.setVisibility(View.VISIBLE);
                DocumentSnapshot document = task.getResult();
                Log.d("TAG", "task " + task.getResult());
                if (document.exists() && document.getData().get("fname").toString() != null
                                        && document.getData().get("lname").toString() != null
                                        && document.getData().get("gender").toString() != null) {
                    profile.setFname(document.getData().get("fname").toString());
                    et_firstName.setText(profile.getFname());
                    profile.setLname(document.getData().get("lname").toString());
                    et_lastname.setText(profile.getLname());
                    profile.setGender(document.getData().get("gender").toString());
                    if (profile.getGender().equals("Male")) {
                        rb_male.setChecked(true);
                    } else {
                        rb_female.setChecked(true);
                    }
                    if (document.getData().get("profileimage") != null) {
                        profile.setProfileimage(document.getData().get("profileimage").toString());
                    }
                    Picasso.get().load(profile.getProfileimage()).into(profilepic);
                    progressBar.setVisibility(View.GONE);
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });

        btn_logout.setOnClickListener(v -> {
            mAuth.signOut();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new DisplayHomeFragment(), "tag_LoginActivity")
                    .addToBackStack(null)
                    .commit();
        });

        profilepic.setOnClickListener(view -> chooseImage());

        btn_save.setOnClickListener(view -> {

            progressBar.setVisibility(View.VISIBLE);
            fname = et_firstName.getText().toString();
            lname = et_lastname.getText().toString();
            email = user.getEmail();
            if (radio_gender.getCheckedRadioButtonId() == R.id.radioMale) {
                gender = "Male";
            } else if (radio_gender.getCheckedRadioButtonId() == R.id.radioFmale) {
                gender = "Female";
            }
            profile.setFname(fname);
            profile.setLname(lname);
            profile.setGender(gender);
            profile.setEmail(email);

            if (et_firstName.getText().toString().equals("")) {
                et_firstName.setError("First Name is required.");
            } else if (et_lastname.getText().toString().equals("")) {
                et_lastname.setError("Last Name is required.");
            } else if (radio_gender.getCheckedRadioButtonId()==-1) {
                Toast.makeText(getActivity(), "Gender is required.", Toast.LENGTH_SHORT).show();
            } else if (filePath == null) {
                if(profile.getProfileimage() != null) {
                    db.collection("Users").document(email)
                            .set(profile)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        mListener.gotoDisplayFragment(profile);
                                    }
                                }
                            });
                }else {
                    Toast.makeText(getActivity(), "Profile pic is required", Toast.LENGTH_SHORT).show();
                }
            }else {
                uploadProfilePicture();
            }
            progressBar.setVisibility(View.GONE);
        });
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
                Picasso.get().load(filePath).into(profilepic);
                profile.setProfileimage(filePath.toString());

        }
        else{
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CreateProfileFragment.OnFragmentInteractionListener) {
            mListener = (CreateProfileFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Profile Page");
    }

    private void uploadProfilePicture(){

        if (filePath == null)
        {
            Toast.makeText(getActivity(), "Please upload a profile picture", Toast.LENGTH_SHORT).show();
        }else {
            StorageReference profilePicture = storageReference.child("profilepic/" + email);
            // Register observers to listen for when the download is done or if it fails
            profilePicture.putFile(filePath).addOnFailureListener(exception -> {
                // Handle unsuccessful uploads
            }).addOnSuccessListener(taskSnapshot -> taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    //progressBar.setVisibility(View.GONE);
                    profile.setProfileimage(task.getResult().toString());

                    db.collection("Users").document(email)
                        .set(profile)
                        .addOnCompleteListener(task1 -> {
                            if(task1.isSuccessful()){
                                Toast.makeText(getActivity(), "Profile Saved Successfully", Toast.LENGTH_SHORT).show();
                                mListener.gotoDisplayFragment(profile);
                            }
                        });
                }
            }));
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(View v);
        void gotoDisplayFragment(Profile profile);
    }

}
