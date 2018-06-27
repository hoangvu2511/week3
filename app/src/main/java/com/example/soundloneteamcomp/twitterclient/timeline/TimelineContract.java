package com.example.soundloneteamcomp.twitterclient.timeline;

import com.example.soundloneteamcomp.twitterclient.base.BasePresenter;
import com.example.soundloneteamcomp.twitterclient.base.BaseView;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;



public interface TimelineContract {
    interface View extends BaseView<Presenter> {
        void onGetStatusesSuccess(List<Tweet> data);
    }

    interface Presenter extends BasePresenter {
        void loadmore();
    }
}
