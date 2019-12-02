package com.ownproj.homework07;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatRoomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatRoomFragment extends Fragment implements  ChatRecyclerView.GetChatDetails{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ImageView iv_send;
    private ImageView iv_sendmedia;
    private EditText et_entermsg;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<Chats> tripchats = new ArrayList<>();
    private OnFragmentInteractionListener mListener;
    private final int PICK_IMAGE = 1;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Trips trips;

    private Uri mImageUri;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String ChatID;
    private String owner;

    public ChatRoomFragment(String ChatID, Trips trips) {
        this.ChatID = ChatID;
        this.trips = trips;
    }
    public ChatRoomFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatRoomFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatRoomFragment newInstance(String param1, String param2) {
        ChatRoomFragment fragment = new ChatRoomFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_chat_room, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        iv_send = getActivity().findViewById(R.id.btn_sendmessage);
        iv_sendmedia = getActivity().findViewById(R.id.btn_sendmedia);
        et_entermsg = getActivity().findViewById(R.id.et_entermessage);
        recyclerView = getActivity().findViewById(R.id.recycler_chat);
        recyclerView.setHasFixedSize(true);
        //use a linear layout manager
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new ChatRecyclerView(tripchats, this);
        recyclerView.setAdapter(mAdapter);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        getActivity().setTitle(trips.getTitle()+" User-"+mAuth.getCurrentUser().getEmail());
        db.collection("ChatRoom")
                .document(ChatID)
                .addSnapshotListener((documentSnapshot, e) -> {
                    if(e != null){
                        return;
                    } else {
                        if(documentSnapshot.getData() != null) {
                            ArrayList<Object> hm = (ArrayList<Object>) documentSnapshot.getData().get("tripChat");
                            tripchats.clear();
                            for(Object o: hm){
                                HashMap<String, String> thm = (HashMap<String, String>) o;
                                Chats chat = new Chats();
                                chat.setOwner(thm.get("owner"));
                                chat.setMessage(thm.get("message"));
                                chat.setDateTime(thm.get("dateTime"));
                                tripchats.add(chat);
                            }
                            mAdapter.notifyDataSetChanged();
                            recyclerView.scrollToPosition(tripchats.size() - 1);
                        }
                    }
                });

        iv_send.setOnClickListener(v -> {
            if (et_entermsg.getText().toString().equals("")
                    || et_entermsg.getText().toString().trim().length() <= 0) {
                et_entermsg.setError("Enter a Message");
            } else {
                final Chats chat = new Chats();
                chat.setDateTime(String.valueOf(new Date().getTime()));
                chat.setOwner(mAuth.getCurrentUser().getEmail());
                if (et_entermsg.getText() != null) {
                    chat.setMessage(et_entermsg.getText().toString());
                    tripchats.add(chat);
                    db.collection("ChatRoom")
                            .document(ChatID)
                            .update("tripChat", tripchats);
                    mAdapter.notifyDataSetChanged();
                    et_entermsg.setText("");
                } else {
                    Toast.makeText(getActivity(), "Type a message or upload a media", Toast.LENGTH_SHORT).show();
                }
            }
        });
        iv_sendmedia.setOnClickListener(v -> openFileChooser());
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            mImageUri = data.getData();

            final Chats chat = new Chats();
            chat.setOwner(mAuth.getCurrentUser().getEmail());
            chat.setDateTime(String.valueOf(new Date().getTime()));

            StorageReference chatImages = mStorageRef.child("chatImages/" + mImageUri);
            chatImages.putFile(mImageUri).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    task.getResult()
                            .getMetadata()
                            .getReference()
                            .getDownloadUrl()
                            .addOnCompleteListener(task1 -> {
                                if(task1.isSuccessful()){
                                    chat.setMessage(task1.getResult().toString());
                                    tripchats.add(chat);
                                    db.collection("ChatRoom")
                                            .document(ChatID)
                                            .update("tripChat", tripchats);
                                    mAdapter.notifyDataSetChanged();
                                }
                            });
                }
            });
        }
    }

    @Override
    public void getChatDetails(final Chats chats){
        if(chats.getOwner().equals(mAuth.getCurrentUser().getEmail())){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Adding Message
            builder.setMessage(R.string.deletemessage)
                    .setTitle(R.string.title);
            // Add the buttons
            builder.setPositiveButton(R.string.delete, (dialog, id) -> deleteChat(chats));
            builder.setNegativeButton(R.string.cancel, (dialog, id) -> {
                // User cancelled the dialog
            });

            // Create the AlertDialog
            AlertDialog dialog = builder.create();
            dialog.show();
            mAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(getActivity(), "Cannot Delete others chat", Toast.LENGTH_SHORT).show();
        }

    }

    private void deleteChat(Chats chats){
        tripchats.remove(chats);
        db.collection("ChatRoom")
                .document(ChatID)
                .update("tripChat", tripchats);
    }

}
