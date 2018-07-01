package com.example.soundloneteamcomp.twitterclient.profile;

import com.example.soundloneteamcomp.twitterclient.base.BasePresenter;
import com.example.soundloneteamcomp.twitterclient.base.BaseView;
import com.example.soundloneteamcomp.twitterclient.model.TweetModel;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

import javax.annotation.Nullable;

public interface ProfileCotract {
    interface View extends BaseView<ProfileCotract.Presenter> {
        void onGetStatusesSuccess(List<TweetModel> data);
        void showInfor(Tweet tweet);
        void onUpdateTweet(TweetModel data,int position);
    }
    interface Presenter extends BasePresenter {
        void getTimeline(@Nullable long id);
        void loadMore(@Nullable long id);

        void updateRetweet(long id,int position);
        void updateUndoRetweet(long id,int postion);

        void updateFav(long id,int position);
        void updateUndoFav(long id,int position);
    }
}
