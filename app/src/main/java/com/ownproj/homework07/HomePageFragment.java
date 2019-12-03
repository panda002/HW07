package com.ownproj.homework07;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;


public class HomePageFragment extends Fragment implements TripsRecyclerView.GetTripDetails {


    private OnFragmentInteractionListener mListener;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<Trips> Trips1;
    private Button bt_logout;
    ImageView iv_createTrip;
    private String email;


    public HomePageFragment(String email) {
        // Required empty public constructor;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        Trips1 = new ArrayList<>();


        bt_logout = getActivity().findViewById(R.id.bt_logout);
        iv_createTrip = getActivity().findViewById(R.id.iv_createTrip);
        recyclerView = getActivity().findViewById(R.id.createtriprecyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        email = mAuth.getCurrentUser().getEmail();
        mAdapter = new TripsRecyclerView(Trips1, this);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        db.collection("Trips").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("TAG", "onActivityCreated HomePageFragment: "+task.getResult());
                            Trips trip = new Trips();
                            trip.setTitle(document.getData().get("title").toString());
                            trip.setLatitude(document.getData().get("latitude").toString());
                            trip.setLongitude(document.getData().get("longitude").toString());
                            trip.setCoverpic(document.getData().get("coverpic").toString());
                            trip.setFriend((ArrayList<String>) document.getData().get("friend"));
                            trip.setTripId(document.getData().get("tripId").toString());
                            trip.setOwner(document.getData().get("owner").toString());
                            Trips1.add(trip);
                        }
                        mAdapter.notifyDataSetChanged();
                    } else {
                        Log.d("Error !!", "Error getting documents: ", task.getException());
                    }
                });

        bt_logout.setOnClickListener(v -> {
            mAuth.signOut();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new DisplayHomeFragment(), "tag_LoginActivity")
                    .addToBackStack(null)
                    .commit();
        });

        iv_createTrip.setOnClickListener(v -> getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new CreateTripFragment(), "tag_CreateTripFragment")
                .addToBackStack(null)
                .commit());


    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Current Trips");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_page, container, false);
    }

    @Override
    public void getTripDetails(Trips trip) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new ViewTripsFragment(trip), "tag_CreateTripFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToTripChatRoom(Trips trip) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new ChatRoomFragment(trip.getTripId(), trip), "tag_ChatRoomFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void deleteTrip(Trips details) {
        if(details.getOwner().equals(mAuth.getCurrentUser().getEmail())) {
            db.collection("Trips").document(details.getTripId())
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            db.collection("ChatRoom")
                                    .document(details.getTripId())
                                    .delete();
                            Toast.makeText(getActivity(), "Trip and Chat Room Deleted", Toast.LENGTH_SHORT).show();
                            mAdapter.notifyDataSetChanged();
                            getFragmentManager().beginTransaction()
                                    .replace(R.id.container, new HomePageFragment(email), "tag_FindTripFragment")
                                    .addToBackStack(null)
                                    .commit();
                            Log.d("TAG", "DocumentSnapshot successfully deleted!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("TAG", "Error deleting document", e);
                        }
                    });
        }else
        {
            Toast.makeText(getActivity(), "Trip can only be deleted by the Owner", Toast.LENGTH_SHORT).show();
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showmessage() {
        Toast.makeText(getActivity(), "No Trips Found. Create One?", Toast.LENGTH_SHORT).show();
    }

    public interface OnFragmentInteractionListener {
    }
}
