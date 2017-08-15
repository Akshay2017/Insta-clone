package com.example.sith3.chatclone.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sith3 on 7/11/2017.
 */

public class Users implements Parcelable {
   public Users(){}

    protected Users(Parcel in) {
        name = in.readString();
        email = in.readString();
        uid=in.readString();
        imageurl=in.readString();
    }

    public static final Creator<Users> CREATOR = new Creator<Users>() {
        @Override
        public Users createFromParcel(Parcel in) {
            return new Users(in);
        }

        @Override
        public Users[] newArray(int size) {
            return new Users[size];
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

    public static Creator<Users> getCREATOR() {
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
