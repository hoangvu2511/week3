package com.example.soundloneteamcomp.twitterclient.login;

import com.example.soundloneteamcomp.twitterclient.base.BasePresenter;
import com.example.soundloneteamcomp.twitterclient.base.BaseView;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterSession;



public interface LoginContract {
    interface View extends BaseView<Presenter> {
        void saveUserSuccess();
    }

    interface Presenter extends BasePresenter {
        void saveResult(Result<TwitterSession> result);
    }
}
