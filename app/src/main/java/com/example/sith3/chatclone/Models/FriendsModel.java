package com.example.sith3.chatclone.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Akshay on 7/28/2017.
 */

public class FriendsModel implements Parcelable {
    public FriendsModel(){}

    protected FriendsModel(Parcel in) {
        name = in.readString();
        email = in.readString();
        uid=in.readString();
        fuid=in.readString();
    }

    public static final Creator<FriendsModel> CREATOR = new Creator<FriendsModel>() {
        @Override
        public FriendsModel createFromParcel(Parcel in) {
            return new FriendsModel(in);
        }

        @Override
        public FriendsModel[] newArray(int size) {
            return new FriendsModel[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String name;

    public static Creator<FriendsModel> getCREATOR() {
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

    public String getFuid() {
        return fuid;
    }

    public void setFuid(String fuid) {
        this.fuid = fuid;
    }

    String fuid;
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
        dest.writeString(fuid);
    }
}
