package com.ownproj.homework07;

import java.util.Map;

public class Profile {
    String fname;
    String lname;
    String gender;
    String profileimage;
    String email;

    public Profile(String fname, String lname, String gender, String profileimage, String email) {
        this.fname = fname;
        this.lname = lname;
        this.gender = gender;
        this.profileimage = profileimage;
        this.email = email;
    }

    public Profile()
    {}

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", gender='" + gender + '\'' +
                ", profileimage=" + profileimage + '\'' +
                ", email=" + email +
                '}';
        }

    public Profile(Map mapProfile)
    {
        this.fname = (String) mapProfile.get("fname");
        this.lname = (String) mapProfile.get("lname");
        this.gender = (String) mapProfile.get("gender");
        this.profileimage = (String) mapProfile.get("profileimage");
        this.email = (String) mapProfile.get("email");
    }


    }
