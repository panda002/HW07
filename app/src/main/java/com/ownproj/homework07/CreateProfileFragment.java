package com.ownproj.homework07;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateProfileFragment extends Fragment {

    private Button btn_save, btn_logout;
    private EditText et_firstName;
    private EditText et_lastname;
    private RadioGroup radio_gender;
    private Profile profile;
    private String fname, lname, email;
    private String gender;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ImageView profilepic;
    private OnFragmentInteractionListener mListener;
    private TextView owneremail;
    private FirebaseAuth mAuth;
    public static final int PICK_IMAGE = 1;


    public CreateProfileFragment() {
        // Required empty public constructor
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
        profilepic.setOnClickListener(view -> chooseImage());
        owneremail = getView().findViewById(R.id.user_email);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        owneremail.setText(user.getEmail());

        btn_save.setOnClickListener(view -> {
            fname = et_firstName.getText().toString();
            lname = et_lastname.getText().toString();
            email = user.getEmail();
            if(radio_gender.getCheckedRadioButtonId() == R.id.radioMale)
            {
                gender = "Male";
            }else if(radio_gender.getCheckedRadioButtonId() == R.id.radioFmale)
            {
                gender = "Female";
            }


            Profile profile = new Profile();
            profile.setFname(fname);
            profile.setLname(lname);
            profile.setGender(gender);
            profile.setImageno(profile.getImageno());
            profile.setEmail(email);

            db.collection("Users")
                    .document(user.getEmail())
                    .set(profile)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            Log.d("TAG", "DocumentSnapshot written with ID: " + task.getResult());
                        }
                    }).addOnFailureListener(e -> Log.d("TAG", "DocumentSnapshot written with ID: " +e));
            //addprofile();
            mListener.gotoDisplayFragment(profile);
        });

        btn_logout.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            DisplayHomeFragment  displayHomeFragment = new DisplayHomeFragment();
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, displayHomeFragment, "tag_DisplayHome")
                    .addToBackStack(null)
                    .commit();

            Toast.makeText(getContext(), "LOGOUT Successful", Toast.LENGTH_SHORT).show();
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

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));

                profilepic = getActivity().findViewById(R.id.iv_selectprofile);
                profilepic.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(View v);
        void gotoDisplayFragment(Profile profile);
    }

}
