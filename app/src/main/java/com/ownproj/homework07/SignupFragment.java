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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignupFragment extends Fragment {



    Button btn_signup, btn_cancel;
    EditText fname, lname, email, pass1, pass2;
    private FirebaseAuth mAuth;
    private SignupFragmentInterface listner;

    public SignupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listner = (SignupFragmentInterface) context;

    }

    @Override
    public void onDetach() {
        super.onDetach();
        listner = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fname = getActivity().findViewById(R.id.su_firstName);
        lname = getActivity().findViewById(R.id.su_lastname);
        email = getActivity().findViewById(R.id.su_email);
        pass1 = getActivity().findViewById(R.id.su_choosePassword);
        pass2 = getActivity().findViewById(R.id.su_confirmPassword);
        btn_signup = getActivity().findViewById(R.id.btnsu_signup);
        btn_cancel = getActivity().findViewById(R.id.btnsu_cancel);

        btn_signup.setOnClickListener(view -> {
            if(authentication()){
                mAuth.createUserWithEmailAndPassword(email.getText().toString(), pass1.getText().toString())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("TAG", "Signup : Success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(getActivity(), "Signup Successfully", Toast.LENGTH_SHORT).show();
                        listner.gotoProfileBuilder();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.d("TAG", "Signup : Failure", task.getException());

                    }

                    // ...
                });
            }

    });

        btn_cancel.setOnClickListener(view -> {
            DisplayHomeFragment  displayHomeFragment = new DisplayHomeFragment();
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, displayHomeFragment, "tag_DisplayHome")
                    .addToBackStack(null)
                    .commit();
        });
    }


    private boolean authentication(){
        if (fname.getText().toString().equals("")
                || fname.getText().toString().trim().length() <= 0) {
            fname.setError("Enter the First Name");
        }else if (lname.getText().toString().equals("")
                || lname.getText().toString().trim().length() <= 0) {
            lname.setError("Enter the Last Name");
        }else if (email.getText().toString().equals("")
                || email.getText().toString().trim().length() <= 0) {
            email.setError("Enter an Email");
        }else if (pass1.getText().toString().equals("")
                || pass1.getText().toString().trim().length() <= 0) {
            pass1.setError("Enter a Password");
        }else if (pass2.getText().toString().equals("")
                || pass2.getText().toString().trim().length() <= 0) {
            pass2.setError("Enter the Password again");
        }else
        {
            return true;
        }
        return false;
    }

    public interface SignupFragmentInterface {
        void gotoProfileBuilder();
    }
}
