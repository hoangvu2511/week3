package com.example.soundloneteamcomp.twitterclient.model;


import android.os.Parcel;
import android.os.Parcelable;

public class TimelineItem implements Parcelable {

    private final String createdAt;
    private final String text;
    private final User_ user;

    public TimelineItem(String createdAt, String text, User_ user) {
        this.createdAt = createdAt;
        this.text = text;
        this.user = user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.createdAt);
        dest.writeString(this.text);
        dest.writeParcelable(this.user, flags);
    }

    protected TimelineItem(Parcel in) {
        this.createdAt = in.readString();
        this.text = in.readString();
        this.user = in.readParcelable(User_.class.getClassLoader());
    }

    public static final Parcelable.Creator<TimelineItem> CREATOR = new Parcelable.Creator<TimelineItem>() {
        @Override
        public TimelineItem createFromParcel(Parcel source) {
            return new TimelineItem(source);
        }

        @Override
        public TimelineItem[] newArray(int size) {
            return new TimelineItem[size];
        }
    };
}
