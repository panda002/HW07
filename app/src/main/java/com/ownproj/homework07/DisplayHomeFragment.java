package com.ownproj.homework07;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.concurrent.Executor;


/**
 * A simple {@link Fragment} subclass.
 */
public class DisplayHomeFragment extends Fragment {

    private FirebaseAuth mAuth;
    Button btn_login;
    Button btn_signup;
    private LoginFragmentInterface listner;
    ProgressBar progressBar;
    static final int GOOGLE_SIGN = 123;
    GoogleSignInClient mGoogleSignInClient;

    public DisplayHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listner = (LoginFragmentInterface) context;

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
        return inflater.inflate(R.layout.fragment_display_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btn_login = getActivity().findViewById(R.id.btn_login);
        btn_signup = getActivity().findViewById(R.id.btn_signup);

        progressBar = getActivity().findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder().requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), googleSignInOptions);

        btn_login.setOnClickListener(view -> SigninGoogle());

        btn_signup.setOnClickListener(view -> listner.goToCreateAccount());
    }



    void SigninGoogle ()
    {
        progressBar.setVisibility(View.VISIBLE);
        Intent signIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signIntent, GOOGLE_SIGN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SIGN) {
            Task<GoogleSignInAccount> task = GoogleSignIn
                    .getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }


    private void firebaseAuthWithGoogle (GoogleSignInAccount account){
        Log.d("TAG", "firebaseAuthWithGoogle: " + account.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener( task -> {
                    if (task.isSuccessful()) {

                        progressBar.setVisibility(View.INVISIBLE);
                        Log.d("TAG", "SignIn Successful");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.d("TAG", "firebaseAuthWithGoogle: " + task.getException());
                        updateUI(null);
                    }
                });
    }

    private void updateUI (FirebaseUser user){
        if (user != null) {
            String name = user.getDisplayName();
            String email = user.getEmail();

            Log.d("TAG", "name : " + name);
            Log.d("TAG", "email : " + email);
            btn_login.setVisibility(View.INVISIBLE);
            listner.gotoProfileBuilder();

        } else {
            btn_login.setVisibility(View.VISIBLE);

        }

    }

    public interface LoginFragmentInterface {
        void goToCreateAccount();
        void gotoProfileBuilder();
    }
}