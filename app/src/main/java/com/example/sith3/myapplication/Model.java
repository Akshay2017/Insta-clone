package com.example.sith3.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sith3 on 7/11/2017.
 */

public class Model implements Parcelable {
   public Model(){}

    protected Model(Parcel in) {
        name = in.readString();
        email = in.readString();
        uid=in.readString();
    }

    public static final Creator<Model> CREATOR = new Creator<Model>() {
        @Override
        public Model createFromParcel(Parcel in) {
            return new Model(in);
        }

        @Override
        public Model[] newArray(int size) {
            return new Model[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String name;

    public static Creator<Model> getCREATOR() {
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
    }
}
