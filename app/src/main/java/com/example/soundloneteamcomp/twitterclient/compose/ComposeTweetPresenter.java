package com.example.soundloneteamcomp.twitterclient.compose;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

import androidx.annotation.NonNull;


public class ComposeTweetPresenter implements ComposeContract.Presenter {

    private final ComposeContract.View mView;
    private TwitterSession mSession = null;
    TwitterApiClient client;
    public ComposeTweetPresenter(@NonNull ComposeContract.View view, TwitterSession session) {
        this.mView = view;
        mSession = session;
        mView.setPresenter(this);
        client = new TwitterApiClient(mSession);
    }

    @Override
    public void sendTweet(String text) {
        mView.showLoading(true);
        client.getStatusesService().update(text, null, null, null, null, null, null, null, null)
                .enqueue(new Callback<Tweet>() {
                    @Override
                    public void success(Result<Tweet> result) {
                        mView.showLoading(false);
                        mView.sendTweetSuccess(result);
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        mView.showLoading(false);
                        mView.showError(exception.getMessage());
                    }
                });
    }

    @Override
    public void getUrl() {
        client.getStatusesService()
                .userTimeline(null,null,null,null,null,null,null,null,null)
                .enqueue(new Callback<List<Tweet>>() {
                    @Override
                    public void success(Result<List<Tweet>> result) {
                        mView.setUrl(result.data.get(0).user);
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
