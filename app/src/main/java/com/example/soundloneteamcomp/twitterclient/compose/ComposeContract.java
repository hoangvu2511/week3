package com.example.soundloneteamcomp.twitterclient.compose;

import com.example.soundloneteamcomp.twitterclient.base.BasePresenter;
import com.example.soundloneteamcomp.twitterclient.base.BaseView;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;


public interface ComposeContract {
    interface View extends BaseView<Presenter> {

        void sendTweetSuccess(Result<Tweet> result);
        void setUrl(User user);
    }

    interface Presenter extends BasePresenter {

        void sendTweet(String text);
        void getUrl();
    }
}
