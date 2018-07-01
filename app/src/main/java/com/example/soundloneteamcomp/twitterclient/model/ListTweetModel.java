package com.example.soundloneteamcomp.twitterclient.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class ListTweetModel implements Parcelable {
    private List<TweetModel> modelList = new ArrayList<>();

    public List<TweetModel> getModelList() {
        return modelList;
    }

    public void setModelList(List<TweetModel> modelList) {
        this.modelList = modelList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.modelList);
    }

    public ListTweetModel() {
    }

    protected ListTweetModel(Parcel in) {
        this.modelList = new ArrayList<TweetModel>();
        in.readList(this.modelList, TweetModel.class.getClassLoader());
    }

    public static final Parcelable.Creator<ListTweetModel> CREATOR = new Parcelable.Creator<ListTweetModel>() {
        @Override
        public ListTweetModel createFromParcel(Parcel source) {
            return new ListTweetModel(source);
        }

        @Override
        public ListTweetModel[] newArray(int size) {
            return new ListTweetModel[size];
        }
    };
}
