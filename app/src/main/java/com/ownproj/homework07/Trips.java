package com.ownproj.homework07;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Trips {

    String title;
    ArrayList<String> friend;
    String owner;
    String coverpic;
    String TripId;
    String  longitude, latitude;


    public Trips(String title, ArrayList<String> friend, String owner, String coverpic, String longitude, String latitude, String TripId) {
        this.title = title;
        this.friend =  friend;
        this.owner = owner;
        this.coverpic = coverpic;
        this.longitude = longitude;
        this.latitude = latitude;
        this.TripId = TripId;
    }

    public Trips() {
    }

    @Override
    public String toString() {
        return "Trips{"+
                    "title ='"+ title + '\'' +
                    ", friend ='"+ friend + '\'' +
                    ", owner ='" + owner + '\'' +
                    ", coverpic ='" + coverpic + '\'' +
                    ", longitude ='"+ longitude + '\'' +
                    ", latitude ='" +latitude+ '\'' +
                    ", TripId ='" +TripId+
        "}";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getFriend() {
        return friend;
    }

    public void setFriend(ArrayList<String> friend) {
        this.friend = friend;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getCoverpic() {
        return coverpic;
    }

    public void setCoverpic(String coverpic) {
        this.coverpic = coverpic;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getTripId() {
        return TripId;
    }

    public void setTripId(String TripId) {
        this.TripId = TripId;
    }



    public Map toHashMap(){
        Map<String, Object> userMap = new HashMap<>();

        userMap.put("coverpic", this.coverpic);
        userMap.put("latitude", this.latitude);
        userMap.put("longitude", this.longitude);
        userMap.put("title", this.title);
        userMap.put("tripId", this.TripId);
        userMap.put("friend", this.friend);
        userMap.put("owner", this.owner);
        return userMap;
    }
}
