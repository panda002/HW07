package com.ownproj.homework07;

// Sidharth Panda
// Nayana Naik
// Groups1 6 HW07
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
        CreateTripFragment.OnFragmentInteractionListener,
        SignupFragment.SignupFragmentInterface,
        ChatRoomFragment.OnFragmentInteractionListener
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

            /*@Override
            public void onBackPressed() {
                backStack();
                new AlertDialog.Builder(this)
                        .setMessage("Are you sure you want to exit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", (dialog, id) -> MainActivity.super.onBackPressed())
                        .setNegativeButton("No", null)
                        .show();
            }
            private void backStack(){
                if(getSupportFragmentManager().getBackStackEntryCount()>1){
                    getSupportFragmentManager().popBackStack();
                }else
                if(getSupportFragmentManager().getBackStackEntryCount()==1){
                    this.finish();
                }
            }*/

            /*boolean doubleBackToExitPressedOnce = false;

            @Override
            public void onBackPressed() {
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                    return;
                }

                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Please click again to go back", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce=false;
                    }
                }, 2000);
            }*/

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
                .addToBackStack(null)
                .commit();
    }



    @Override
    public void gotoCreatetrip() {

        CreateTripFragment createTripFragment = new CreateTripFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, createTripFragment, "tag_CreateTrip")
                .addToBackStack(null)
                .commit();
    }

            @Override
            public void onFragmentInteraction(View v) {

            }
            @Override
            public void onFragmentInteraction(Uri uri) {
            }

            @Override
            public void gotoDisplayFragment() {
                DisplayProfileFragment  displayProfileFragment = new DisplayProfileFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, displayProfileFragment, "tag_DislpayProfile")
                        .addToBackStack(null)
                        .commit();
            }

            @Override
            public void gotoTripBuilder(Trips trip) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new ViewTripsFragment(trip), "tag_CreateTripFragment")
                        .addToBackStack(null)
                        .commit();
            }


        }

