package com.example.soundloneteamcomp.twitterclient.profile;

import com.example.soundloneteamcomp.twitterclient.util.ConvertTwitterHelper;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ProfilePresenter implements ProfileCotract.Presenter{

    TwitterApiClient client = null;
    ProfileCotract.View mView;
    int count = 20;
    ConvertTwitterHelper convert = new ConvertTwitterHelper();

    public ProfilePresenter(@NonNull ProfileCotract.View view, TwitterSession session){
        mView= view;
        mView.setPresenter(this);
        client = new TwitterApiClient(session);

    }


    @Override
    public void getTimeline(@Nullable long id) {
        if (id!=1){
            client.getStatusesService()
                    .userTimeline(id,null,count,null,null,null,null,null,null)
                    .enqueue(new Callback<List<Tweet>>() {
                        @Override
                        public void success(Result<List<Tweet>> result) {
                            mView.onGetStatusesSuccess(new ConvertTwitterHelper().ConvertList(result.data));
                            mView.showInfor(result.data.get(0).user.name);
                        }

                        @Override
                        public void failure(TwitterException exception) {

                        }
                    });

        }
        else
            client.getStatusesService()
                    .userTimeline(null,null,count,null,null,null,null,null,null)
                    .enqueue(new Callback<List<Tweet>>() {
                        @Override
                        public void success(Result<List<Tweet>> result) {
                            mView.onGetStatusesSuccess(new ConvertTwitterHelper().ConvertList(result.data));
                            mView.showInfor(result.data.get(0).user.name);
                        }

                        @Override
                        public void failure(TwitterException exception) {

                        }
                    });
    }

    @Override
    public void loadMore(@Nullable long id) {
        count+=20;
        if (id != 0)
            client.getStatusesService()
                    .userTimeline(id,null,count,null,null,null,null,null,null)
                    .enqueue(new Callback<List<Tweet>>() {
                        @Override
                        public void success(Result<List<Tweet>> result) {
                            mView.onGetStatusesSuccess(convert.ConvertList(result.data));
                        }

                        @Override
                        public void failure(TwitterException exception) {

                        }
                    });
        else
            client.getStatusesService()
                    .userTimeline(null,null,count,null,null,null,null,null,null)
                    .enqueue(new Callback<List<Tweet>>() {
                        @Override
                        public void success(Result<List<Tweet>> result) {
                            mView.onGetStatusesSuccess(convert.ConvertList(result.data));
                        }

                        @Override
                        public void failure(TwitterException exception) {

                        }
                    });
    }




    @Override
    public void updateRetweet(long id,int position){
        client.getStatusesService()
                .retweet(id,null).enqueue(new Callback<Tweet>() {
            @Override
            public void success(Result<Tweet> result) {
                mView.onUpdateTweet(convert.ConvertTweet(result.data),position);
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
                        mView.onUpdateTweet(convert.ConvertTweet(result.data),postion);
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
                        mView.onUpdateTweet(convert.ConvertTweet(result.data),position);
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
                        mView.onUpdateTweet(convert.ConvertTweet(result.data),position);
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
