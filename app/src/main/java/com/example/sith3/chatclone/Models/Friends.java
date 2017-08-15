package com.example.sith3.chatclone.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sith3 on 7/11/2017.
 */

public class Friends implements Parcelable {
   public Friends(){}

    protected Friends(Parcel in) {
        name = in.readString();
        email = in.readString();
        uid=in.readString();
        imageurl=in.readString();
    }

    public static final Creator<Friends> CREATOR = new Creator<Friends>() {
        @Override
        public Friends createFromParcel(Parcel in) {
            return new Friends(in);
        }

        @Override
        public Friends[] newArray(int size) {
            return new Friends[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String name;

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    String imageurl;

    public static Creator<Friends> getCREATOR() {
        return CREATOR;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    String uid;
    String email;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(uid);
        dest.writeString(imageurl);
    }
}
