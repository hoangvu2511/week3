package com.example.soundloneteamcomp.twitterclient.profile;

import com.example.soundloneteamcomp.twitterclient.base.BasePresenter;
import com.example.soundloneteamcomp.twitterclient.base.BaseView;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

public interface ProfileCotract {
    interface View extends BaseView<ProfileCotract.Presenter> {
        void onGetStatusesSuccess(List<Tweet> data);
        void showInfor(Tweet tweet);
    }
    interface Presenter extends BasePresenter {
        void getTimeline(long id);
    }
}
