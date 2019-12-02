package com.ownproj.homework07;
//code reference taken from https://github.com/sameershanbhag/MAD_HW07_PartA
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.sql.Date;
import java.util.ArrayList;

public class ChatRecyclerView extends RecyclerView.Adapter<ChatRecyclerView.MyViewHolder> {
    private ArrayList<Chats> chatDetails;

    private FirebaseAuth mAuth;

    private GetChatDetails getChatDetails;

    public ChatRecyclerView(ArrayList<Chats> chatDetails, GetChatDetails getChatDetails) {
        this.chatDetails = chatDetails;
        this.getChatDetails =  getChatDetails;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_items, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final Chats details = chatDetails.get(position);

        mAuth = FirebaseAuth.getInstance();
        PrettyTime prettyTime = new PrettyTime();
        String owner = mAuth.getCurrentUser().getEmail();


        if(details.getOwner().equals(owner)) {
            if (details.getMessage().startsWith("https://")) {
                holder.receiver_message_text.setVisibility(View.GONE);
                holder.sender_messsage_text.setVisibility(View.GONE);
                holder.message_receiver_image_view.setVisibility(View.GONE);
                Picasso.get().load(details.getMessage()).resize(50,50).into(holder.message_sender_image_view);
                holder.message_sender_image_view.setVisibility(View.VISIBLE);
                holder.tv_time2.setVisibility(View.VISIBLE);
                holder.tv_username2.setVisibility(View.VISIBLE);
                holder.tv_time2.setText(prettyTime.format(new Date(Long.parseLong(details.getDateTime()))));
                holder.tv_username2.setText(details.getOwner());
                holder.tv_time.setVisibility(View.GONE);
                holder.tv_username.setVisibility(View.GONE);
            } else {
                holder.message_sender_image_view.setVisibility(View.GONE);
                holder.receiver_message_text.setVisibility(View.GONE);
                holder.message_receiver_image_view.setVisibility(View.GONE);
                holder.sender_messsage_text.setVisibility(View.VISIBLE);
                holder.sender_messsage_text.setText(details.getMessage());
                holder.tv_time2.setVisibility(View.VISIBLE);
                holder.tv_username2.setVisibility(View.VISIBLE);
                holder.tv_time2.setText(prettyTime.format(new Date(Long.parseLong(details.getDateTime()))));
                holder.tv_username2.setText(details.getOwner());
                holder.tv_time.setVisibility(View.GONE);
                holder.tv_username.setVisibility(View.GONE);
            }
            holder.itemView.setOnLongClickListener(v -> {
                getChatDetails.getChatDetails(details);
                return false;
            });
        }else{
            if (details.getMessage().startsWith("https://")) {
                holder.receiver_message_text.setVisibility(View.GONE);
                holder.sender_messsage_text.setVisibility(View.GONE);
                holder.message_receiver_image_view.setVisibility(View.VISIBLE);
                Picasso.get().load(details.getMessage()).resize(50,50).into(holder.message_receiver_image_view);
                holder.tv_time.setVisibility(View.VISIBLE);
                holder.tv_username.setVisibility(View.VISIBLE);
                holder.tv_time2.setVisibility(View.GONE);
                holder.tv_username2.setVisibility(View.GONE);
                holder.tv_time.setText(prettyTime.format(new Date(Long.parseLong(details.getDateTime()))));
                holder.tv_username.setText(details.getOwner());
            } else {
                holder.message_sender_image_view.setVisibility(View.GONE);
                holder.message_receiver_image_view.setVisibility(View.GONE);
                holder.sender_messsage_text.setVisibility(View.GONE);
                holder.receiver_message_text.setVisibility(View.VISIBLE);
                holder.receiver_message_text.setText(details.getMessage());
                holder.tv_time.setVisibility(View.VISIBLE);
                holder.tv_username.setVisibility(View.VISIBLE);
                holder.tv_time2.setVisibility(View.GONE);
                holder.tv_username2.setVisibility(View.GONE);
                holder.tv_time.setText(prettyTime.format(new Date(Long.parseLong(details.getDateTime()))));
                holder.tv_username.setText(details.getOwner());
            }
            holder.itemView.setOnLongClickListener(v -> {
                getChatDetails.getChatDetails(details);
                return false;
            });
        }

    }

    @Override
    public int getItemCount() {
        return chatDetails.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final Context context;

        ImageView message_receiver_image_view,message_sender_image_view;
        TextView receiver_message_text,sender_messsage_text;
        TextView tv_time,tv_time2;
        TextView tv_username, tv_username2;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();

            message_receiver_image_view = itemView.findViewById(R.id.message_receiver_image_view);
            message_sender_image_view = itemView.findViewById(R.id.message_sender_image_view);
            receiver_message_text = itemView.findViewById(R.id.receiver_message_text);
            sender_messsage_text = itemView.findViewById(R.id.sender_messsage_text);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_time2 = itemView.findViewById(R.id.tv_time2);
            tv_username = itemView.findViewById(R.id.tv_username);
            tv_username2 = itemView.findViewById(R.id.tv_username2);
        }
    }

    public interface GetChatDetails {
        void getChatDetails(Chats chats);
    }

}

