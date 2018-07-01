package com.example.soundloneteamcomp.twitterclient.timeline;

import com.example.soundloneteamcomp.twitterclient.base.BasePresenter;
import com.example.soundloneteamcomp.twitterclient.base.BaseView;
import com.example.soundloneteamcomp.twitterclient.model.TweetModel;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;



public interface TimelineContract {
    interface View extends BaseView<Presenter> {
        void onGetStatusesSuccess(List<Tweet> data);
        void onUpdateTweet(TweetModel tweet, int position);
    }

    interface Presenter extends BasePresenter {
        void loadmore();

        void updateRetweet(long id,int position);
        void updateUndoRetweet(long id,int postion);

        void updateFav(long id,int position);
        void updateUndoFav(long id,int position);
    }
}
