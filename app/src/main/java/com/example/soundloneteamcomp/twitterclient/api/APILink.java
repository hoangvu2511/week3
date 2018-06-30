package com.example.soundloneteamcomp.twitterclient.api;

import com.example.soundloneteamcomp.twitterclient.model.User_;
import com.twitter.sdk.android.core.models.Tweet;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface APILink {
    String BASE_URL = "http://api.twitter.com/";

    @GET("/1.1/users/show.json")
    Call<User_> show(@Query("user_id") long id);

    @POST("/1.1/users/show.json")
    Call<Tweet> favor(@Query("id") long id);
}
