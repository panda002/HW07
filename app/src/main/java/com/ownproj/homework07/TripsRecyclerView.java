package com.ownproj.homework07;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TripsRecyclerView extends RecyclerView.Adapter<TripsRecyclerView.MyViewHolder> {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private ArrayList<Trips> tripDetails;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static GetTripDetails TListner;

    public TripsRecyclerView(ArrayList<Trips> trackDetails, GetTripDetails TripDetailsListner) {
        this.tripDetails = trackDetails;
        TripsRecyclerView.TListner =  TripDetailsListner;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trip_items, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final Trips details = tripDetails.get(position);
        if(details==null)
        {
            TListner.showmessage();
        }
        //TODO: Trip Population
        holder.tv_triptitle.setText(details.getTitle());
        Picasso.get().load(details.getCoverpic()).into(holder.iv_tripcover);

        if(!details.getFriend().contains(mAuth.getCurrentUser().getEmail())){
            holder.btn_joinchat.setEnabled(false);
        }

        holder.btn_joinchat.setOnClickListener(v -> {
            Log.d("Trips", details.toString());
            if(details.getFriend().contains(mAuth.getCurrentUser().getEmail())){
                TListner.goToTripChatRoom(details);
            }
        });

        holder.btn_viewtripdetails.setOnClickListener(v -> TListner.getTripDetails(details));
        holder.btn_deletetrip.setOnClickListener(view -> TListner.deleteTrip(details));
    }

    @Override
    public int getItemCount() {
        return tripDetails.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final Context context;

        TextView tv_triptitle;
        Button btn_viewtripdetails, btn_joinchat, btn_deletetrip;
        ImageView iv_tripcover;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            tv_triptitle = itemView.findViewById(R.id.tv_triptitle);
            btn_viewtripdetails = itemView.findViewById(R.id.btn_viewtripdetails);
            btn_joinchat = itemView.findViewById(R.id.btn_joinchat);
            iv_tripcover = itemView.findViewById(R.id.iv_tripcover);
            btn_deletetrip = itemView.findViewById(R.id.btn_deletetrip);

        }
    }

    public interface GetTripDetails {
        void getTripDetails(Trips trip);
        void goToTripChatRoom(Trips trip);
        void deleteTrip(Trips details);
        void showmessage();
    }

}
