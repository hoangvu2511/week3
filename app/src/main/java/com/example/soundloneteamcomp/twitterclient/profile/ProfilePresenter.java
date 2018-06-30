package com.example.soundloneteamcomp.twitterclient.profile;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

import androidx.annotation.NonNull;

public class ProfilePresenter implements ProfileCotract.Presenter{

    TwitterApiClient client = null;
    ProfileCotract.View mView;

    public ProfilePresenter(@NonNull ProfileCotract.View view, TwitterSession session){
        mView= view;
        mView.setPresenter(this);
        client = new TwitterApiClient(session);

    }

    @Override
    public void getTimeline(long id) {
        client.getStatusesService()
                .userTimeline(id,null,null,null,null,null,null,null,null)
                .enqueue(new Callback<List<Tweet>>() {
                    @Override
                    public void success(Result<List<Tweet>> result) {
                        mView.onGetStatusesSuccess(result.data);
                    }

                    @Override
                    public void failure(TwitterException exception) {

                    }
                });
    }

    @Override
    public void start() {

    }
}
