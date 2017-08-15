package com.example.sith3.chatclone.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Akshay on 8/2/2017.
 */

public class Post implements Parcelable {

    public Post() {
    }
    String caption;
    String imageurl;
    String postkey;

    protected Post(Parcel in) {
        caption = in.readString();
        imageurl = in.readString();
        postkey = in.readString();
        useruid = in.readString();
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getPostkey() {
        return postkey;
    }

    public void setPostkey(String postkey) {
        this.postkey = postkey;
    }

    public String getUseruid() {
        return useruid;
    }

    public void setUseruid(String useruid) {
        this.useruid = useruid;
    }

    String useruid;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(caption);
        dest.writeString(imageurl);
        dest.writeString(postkey);
        dest.writeString(useruid);
    }
}
