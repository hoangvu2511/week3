package com.example.soundloneteamcomp.twitterclient.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User_ implements Parcelable {

    private final String name;
    private final String screenName;
    private final String profileImageUrl;

    public User_(String name, String screenName, String profileImageUrl) {
        this.name = name;
        this.screenName = screenName;
        this.profileImageUrl = profileImageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.screenName);
        dest.writeString(this.profileImageUrl);
    }

    protected User_(Parcel in) {
        this.name = in.readString();
        this.screenName = in.readString();
        this.profileImageUrl = in.readString();
    }

    public static final Parcelable.Creator<User_> CREATOR = new Parcelable.Creator<User_>() {
        @Override
        public User_ createFromParcel(Parcel source) {
            return new User_(source);
        }

        @Override
        public User_[] newArray(int size) {
            return new User_[size];
        }
    };
}
