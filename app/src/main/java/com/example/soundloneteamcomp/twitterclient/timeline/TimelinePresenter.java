package com.example.soundloneteamcomp.twitterclient.timeline;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

import androidx.annotation.NonNull;

public class TimelinePresenter implements TimelineContract.Presenter {
    TwitterApiClient client = null;
    TimelineContract.View mView;
    int count = 30;

    public TimelinePresenter(@NonNull TimelineContract.View view, TwitterSession session){
        mView= view;
        mView.setPresenter(this);
        client = new TwitterApiClient(session);

    }

    @Override
    public void start() {
        mView.showLoading(true);
        count = 30;
        client.getStatusesService()
                .homeTimeline(count, null, null, null, null, null, null)
                .enqueue(new Callback<List<Tweet>>() {
                    @Override
                    public void success(Result<List<Tweet>> result) {
                        mView.showLoading(false);
                        mView.onGetStatusesSuccess(result.data);
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        mView.showLoading(false);
                        mView.showError(exception.getMessage());
                    }
                });
    }

    @Override
    public void loadmore() {
        count+=20;
        mView.showLoading(true);
        client.getStatusesService()
                .homeTimeline(count, null, null, null, null, null, null)
                .enqueue(new Callback<List<Tweet>>() {
                    @Override
                    public void success(Result<List<Tweet>> result) {
                        mView.showLoading(false);
                        count+=20;
                        mView.onGetStatusesSuccess(result.data);
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        mView.showLoading(false);
                        mView.showError(exception.getMessage());
                    }
                });
    }

    @Override
    public void updateRetweet(long id,int position){
        client.getStatusesService()
                .retweet(id,null).enqueue(new Callback<Tweet>() {
            @Override
            public void success(Result<Tweet> result) {
                mView.onUpdateTweet(result.data,position);
            }

            @Override
            public void failure(TwitterException exception) {

            }
        });
    }

    @Override
    public void updateUndoRetweet(long id, int postion) {
        client.getStatusesService()
                .unretweet(id,null)
                .enqueue(new Callback<Tweet>() {
                    @Override
                    public void success(Result<Tweet> result) {
                        mView.onUpdateTweet(result.data,postion);
                    }

                    @Override
                    public void failure(TwitterException exception) {

                    }
                });
    }

    @Override
    public void  updateFav(long id, int position){
        client.getFavoriteService()
                .create(id,null)
                .enqueue(new Callback<Tweet>() {
                    @Override
                    public void success(Result<Tweet> result) {
                            mView.onUpdateTweet(result.data,position);
                    }

                    @Override
                    public void failure(TwitterException exception) {

                    }
                });
    }

    @Override
    public void updateUndoFav(long id, int position) {
        client.getFavoriteService()
                .destroy(id,null)
                .enqueue(new Callback<Tweet>() {
                    @Override
                    public void success(Result<Tweet> result) {
                        mView.onUpdateTweet(result.data,position);
                    }

                    @Override
                    public void failure(TwitterException exception) {

                    }
                });
    }

}
