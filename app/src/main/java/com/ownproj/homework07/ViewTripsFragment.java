package com.ownproj.homework07;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewTripsFragment#} factory method to
 * create an instance of this fragment.
 */
public class ViewTripsFragment extends Fragment {

    private HomePageFragment.OnFragmentInteractionListener mListener;

    private Trips trips;
    private ImageView iv_coverphoto;
    private TextView name, latitude, longitude, viewtrip_owner;
    private Button btn_chatjoin;
    private Button btn_tripjoin, btn_leavetrip;
    private FirebaseAuth mAuth;
    private String owner;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ViewTripsFragment(Trips trips) {
        this.trips = trips;
    }
    ArrayList<Chats> chatList =  new ArrayList<>();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        iv_coverphoto = getActivity().findViewById(R.id.viewtrip_photo);
        name = getActivity().findViewById(R.id.viewtrip_title);
        latitude = getActivity().findViewById(R.id.viewtrip_latitude);
        longitude = getActivity().findViewById(R.id.viewtrip_longitude);
        btn_chatjoin = getActivity().findViewById(R.id.btn_chatroom);
        btn_tripjoin = getActivity().findViewById(R.id.btn_jointrip);
        btn_leavetrip = getActivity().findViewById(R.id.btn_leavetrip);
        viewtrip_owner= getActivity().findViewById(R.id.viewtrip_owner);

        Picasso.get().load(trips.getCoverpic()).into(iv_coverphoto);

        mAuth = FirebaseAuth.getInstance();

        owner = mAuth.getCurrentUser().getEmail();
        name.setText(trips.getTitle());
        latitude.setText(trips.getLatitude());
        longitude.setText(trips.getLongitude());
        viewtrip_owner.setText(trips.getOwner());
        if(!trips.getFriend().contains(mAuth.getCurrentUser().getEmail())){
            btn_chatjoin.setEnabled(false);
            btn_leavetrip.setEnabled(false);
        }

        if(trips.getFriend().contains(mAuth.getCurrentUser().getEmail())){
            btn_tripjoin.setEnabled(false);
            btn_leavetrip.setEnabled(true);
        }


        db.collection("ChatRoom")
                .document(trips.getTripId())
                .addSnapshotListener((documentSnapshot, e) -> {
                    if(e != null){
                        return;
                    } else {
                        if(documentSnapshot.getData() != null){
                            ArrayList<Object> hm = (ArrayList<Object>) documentSnapshot.getData().get("tripChat");
                            for(Object a: hm){
                                HashMap<String, String> tempHashMap = (HashMap<String, String>) a;
                                Chats Chat = new Chats();
                                Chat.setOwner(tempHashMap.get("owner"));
                                Chat.setMessage(tempHashMap.get("message"));
                                Chat.setDateTime(tempHashMap.get("dateTime"));
                                chatList.add(Chat);
                            }
                        }
                    }
                });

        btn_chatjoin.setOnClickListener(v -> {

            final ChatRoom ChatRoom = new ChatRoom();
            ChatRoom.setTripId(trips.getTripId());
            ChatRoom.setFriends(trips.getFriend());
            ChatRoom.setTripChat(chatList);

            db.collection("ChatRoom")
                    .document(trips.getTripId())
                    .set(ChatRoom.toHashMap())
                    .addOnCompleteListener(task -> getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, new ChatRoomFragment(trips.getTripId(), trips), "tag_ChatRoomFragment")
                            .addToBackStack(null)
                            .commit());
        });

        btn_tripjoin.setOnClickListener(view -> {
            ArrayList<String> friendlist = trips.getFriend();
            friendlist.add(mAuth.getCurrentUser().getEmail());
            trips.setFriend(friendlist);

            db.collection("Trips")
                    .document(trips.getTripId())
                    .set(trips.toHashMap())
                    .addOnCompleteListener(task -> {
                        Toast.makeText(getActivity(), "Trip Joined", Toast.LENGTH_SHORT).show();
                        btn_tripjoin.setEnabled(false);
                        btn_chatjoin.setEnabled(true);
                        btn_leavetrip.setEnabled(true);
                    });

        });
        btn_leavetrip.setOnClickListener(view -> {
            ArrayList<String> friendlist = trips.getFriend();
            friendlist.remove(mAuth.getCurrentUser().getEmail());
            trips.setFriend(friendlist);

            db.collection("Trips")
                    .document(trips.getTripId())
                    .set(trips.toHashMap())
                    .addOnCompleteListener(task -> {
                        Toast.makeText(getActivity(), "Trip Left", Toast.LENGTH_SHORT).show();
                        btn_tripjoin.setEnabled(true);
                        btn_chatjoin.setEnabled(false);
                        btn_leavetrip.setEnabled(false);
                    });

        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Trip Details | User - "+mAuth.getCurrentUser().getEmail());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_trips, container, false);
    }

    public interface OnFragmentInteractionListener {
    }
}
