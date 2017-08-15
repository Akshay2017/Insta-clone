package com.example.sith3.chatclone.Models;

/**
 * Created by Akshay on 8/5/2017.
 */

public class Comment {
    String uid;

    public String getGetpostkey() {
        return getpostkey;
    }

    public void setGetpostkey(String getpostkey) {
        this.getpostkey = getpostkey;
    }

    String getpostkey;

    public Comment() {
    }

    public String getUid() {

        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMeassage() {
        return meassage;
    }

    public void setMeassage(String meassage) {
        this.meassage = meassage;
    }

    String meassage;
}
