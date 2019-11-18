package com.ownproj.homework07;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class MainActivity
        extends
        AppCompatActivity
        implements
        DisplayHomeFragment.LoginFragmentInterface,
        CreateProfileFragment.OnFragmentInteractionListener,
        DisplayProfileFragment.OnFragmentInteractionListener,
        CreateTrip.OnFragmentInteractionListener
        {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayHomeFragment  displayHomeFragment = new DisplayHomeFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, displayHomeFragment, "tag_DisplayHome")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToCreateAccount() {

        SignupFragment signupFragment = new SignupFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, signupFragment, "tag_SignUp")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void gotoProfileBuilder() {

        CreateProfileFragment  createProfileFragment = new CreateProfileFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, createProfileFragment, "tag_ProfileBuilder")
                .addToBackStack(null)
                .commit();
    }


    @Override
    public void gotoDisplayFragment(Profile profile) {
        DisplayProfileFragment  displayProfileFragment = new DisplayProfileFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, displayProfileFragment, "tag_DislpayProfile")
                .addToBackStack("tag_ProfileBuilder")
                .commit();
    }



    @Override
    public void gotoCreatetrip() {

        CreateTrip createTrip = new CreateTrip();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, createTrip, "tag_CreateTrip")
                .addToBackStack("tag_DislpayProfile")
                .commit();
    }

            @Override
            public void onFragmentInteraction(View v) {

            }
            @Override
            public void onFragmentInteraction(Uri uri) {
            }
}

