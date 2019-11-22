package com.ownproj.homework07;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class CreateTrip extends Fragment {


    private OnFragmentInteractionListener mListener;
    private ImageView  addcoverpic;
    private EditText title, friend;
    private String owner, coverpic;
    private EditText  longitute, latitude;
    public static final int PICK_IMAGE = 1;
    private Spinner spinner;
    private Button createchat, savetrip;

    public CreateTrip() {
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
        friend = getActivity().findViewById(R.id.Trip_title);
        latitude = getActivity().findViewById(R.id.Trip_latitude);
        longitute = getActivity().findViewById(R.id.Trip_longitude);
        spinner = getActivity().findViewById(R.id.spinner);
        addcoverpic = getActivity().findViewById(R.id.Trip_iv_tripphoto);
        savetrip = getActivity().findViewById(R.id.Trip_btn_Savetrip);
        createchat = getActivity().findViewById(R.id.Trip_btn_createchat);

        addcoverpic.setOnLongClickListener(view -> {
            chooseImage();
            return false;

        });

        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference subjectsRef = rootRef.collection("Users");

        List<String> users = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, users);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        subjectsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String subject = document.getString("fname");
                    users.add(subject);
                }
                adapter.notifyDataSetChanged();
            }
        });


        savetrip.setOnClickListener(view -> {

            Trips trips = new Trips();
            trips.setTitle(title.getText().toString());
            trips.setLatitude(Integer.parseInt(latitude.getText().toString()));
            trips.setLongitute(Integer.parseInt(longitute.getText().toString()));



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

                addcoverpic = getActivity().findViewById(R.id.Trip_iv_tripphoto);
                addcoverpic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
    }
}
