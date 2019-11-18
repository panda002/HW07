package com.ownproj.homework07;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;



/**
 * A simple {@link Fragment} subclass.
 */
public class CreateProfileFragment extends Fragment {

    private Button btn_save;
    private EditText et_firstName;
    private EditText et_lastname;
    private RadioGroup radio_gender;
    private Profile profile;
    private String fname, lname;
    private String gender;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ImageView profilepic;
    private OnFragmentInteractionListener mListener;



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


        btn_save.setOnClickListener(view -> {
            fname = et_firstName.getText().toString();
            lname = et_lastname.getText().toString();
            if(radio_gender.getCheckedRadioButtonId() == R.id.radioMale)
            {
                gender = "Male";
            }else if(radio_gender.getCheckedRadioButtonId() == R.id.radioFmale)
            {
                gender = "Female";
            }

            String pid = fname + lname + gender;

            Profile profile = new Profile();
            profile.setFname(fname);
            profile.setLname(lname);
            profile.setGender(gender);
            profile.setImageno(R.drawable.common_google_signin_btn_icon_dark);
            profile.setPid(pid);

            db.collection("Users")
                    .document(profile.getPid())
                    .set(profile)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            Log.d("TAG", "DocumentSnapshot written with ID: " + task.getResult());
                        }
                    }).addOnFailureListener(e -> Log.d("TAG", "DocumentSnapshot written with ID: " +e));
            //addprofile();
            mListener.gotoDisplayFragment(profile);
        });
    }

    /*private void addprofile() {
        fname = et_firstName.getText().toString();
        lname = et_lastname.getText().toString();
        if(radio_gender.getCheckedRadioButtonId() == R.id.radioMale)
        {
            gender = "Male";
        }else if(radio_gender.getCheckedRadioButtonId() == R.id.radioFmale)
        {
            gender = "Female";
        }

        String pid = fname + lname + gender;

        Profile profile = new Profile();
        profile.setFname(fname);
        profile.setLname(lname);
        profile.setGender(gender);
        profile.setImageno(R.drawable.common_google_signin_btn_icon_dark);
        profile.setPid(pid);

        db.collection("Users")
                .document(profile.getPid())
                .set(profile)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Log.d("TAG", "DocumentSnapshot written with ID: " + task.getResult());
                    }
                }).addOnFailureListener(e -> Log.w("TAG", "Error adding document", e));
    }*/


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
