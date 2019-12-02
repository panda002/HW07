package com.ownproj.homework07;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatRoom {
    String TripId;
    ArrayList<Chats> tripChat;
    ArrayList<String> friends;

    public ArrayList<Chats> getTripChat() {
        return tripChat;
    }

    public void setTripChat(ArrayList<Chats> tripChat) {
        this.tripChat = tripChat;
    }

    public String getTripId() {
        return TripId;
    }

    public void setTripId(String tripId) {
        TripId = tripId;
    }

    public ArrayList<String> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<String> friends) {
        friends = friends;
    }

    public Map toHashMap(){
        Map<String, Object> userMap = new HashMap<>();

        userMap.put("tripChat", this.tripChat);
        userMap.put("tripId", this.TripId);
        userMap.put("friends", this.friends);
        return userMap;
    }
}
