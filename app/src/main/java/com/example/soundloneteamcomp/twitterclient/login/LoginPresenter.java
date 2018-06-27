package com.example.soundloneteamcomp.twitterclient.login;

import com.example.soundloneteamcomp.twitterclient.util.SharedPreferenceHelper;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterSession;


public class LoginPresenter implements LoginContract.Presenter {
    LoginContract.View view;

    public LoginPresenter(LoginContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void saveResult(Result<TwitterSession> result) {
        SharedPreferenceHelper.getInstance().saveUserName(result.data.getUserName());
        view.saveUserSuccess();
    }

    @Override
    public void start() {

    }
}
