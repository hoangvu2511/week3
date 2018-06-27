package com.example.soundloneteamcomp.twitterclient.api;

import com.example.soundloneteamcomp.twitterclient.model.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface APILink {
    String BASE_URL = "http://api.twitter.com/";

    @GET("/1.1/users/show.json")
    Call<User> show(@Query("user_id") long id);
}
