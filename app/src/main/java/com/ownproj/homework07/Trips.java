package com.ownproj.homework07;


public class Trips {

    String title, friend, owner, coverpic;
    int  longitute, latitude;


    public Trips(String title, String friend, String owner, String coverpic, int longitute, int latitude) {
        this.title = title;
        this.friend = friend;
        this.owner = owner;
        this.coverpic = coverpic;
        this.longitute = longitute;
        this.latitude = latitude;
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
                    ", longitute ='"+ longitute + '\'' +
                    ", latitude ='" +latitude+
        "}";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFriend() {
        return friend;
    }

    public void setFriend(String friend) {
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

    public int getLongitute() {
        return longitute;
    }

    public void setLongitute(int longitute) {
        this.longitute = longitute;
    }

    public int getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }
}
