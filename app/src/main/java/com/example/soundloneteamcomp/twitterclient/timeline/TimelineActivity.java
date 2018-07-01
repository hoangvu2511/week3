package com.example.soundloneteamcomp.twitterclient.timeline;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.soundloneteamcomp.twitterclient.R;
import com.example.soundloneteamcomp.twitterclient.adapter.EndlessRecyclerViewScrollListener;
import com.example.soundloneteamcomp.twitterclient.adapter.TimelineComplexAdapter;
import com.example.soundloneteamcomp.twitterclient.compose.ComposeTweetActivity;
import com.example.soundloneteamcomp.twitterclient.model.TweetModel;
import com.example.soundloneteamcomp.twitterclient.profile.ProfileActivity;
import com.example.soundloneteamcomp.twitterclient.util.ConvertTwitterHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


public class TimelineActivity extends AppCompatActivity implements TimelineContract.View {
    private static String TAG = TimelineActivity.class.getSimpleName();
    RecyclerView rvTimeline;
    ProgressBar loader;
    FloatingActionButton fab,gotoMyProfile;
    TimelineContract.Presenter presenter;
    TimelineComplexAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    EndlessRecyclerViewScrollListener scrollListener;

    List<TweetModel> tweetModelList = new ArrayList<>();
    LinearLayoutManager manager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        rvTimeline = findViewById(R.id.rvTimeline);
        swipeRefreshLayout = findViewById(R.id.swipeLayout);
        loader = findViewById(R.id.loader);
        fab = findViewById(R.id.fab);
        gotoMyProfile = findViewById(R.id.gotoProfile);
        presenter = new TimelinePresenter(this, TwitterCore.getInstance().getSessionManager().getActiveSession());

        fab.setOnClickListener(view -> {
           startActivity(new Intent(this, ComposeTweetActivity.class));
           overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right );
        });

        gotoMyProfile.setOnClickListener(view -> {
            startActivity(new Intent(this, ProfileActivity.class).putExtra("userId",0));
            overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right );
        });

        setRecyclerView();
        if (savedInstanceState == null)
            presenter.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void setPresenter(TimelineContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showLoading(boolean isShow) {
        loader.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onGetStatusesSuccess(List<Tweet> data) {
        Log.d(TAG, "Loaded " + data.size());
        swipeRefreshLayout.setRefreshing(false);
        adapter.setData(new ConvertTwitterHelper().ConvertList(data));
        tweetModelList = adapter.getData();
    }

    @Override
    public void onUpdateTweet(TweetModel tweet, int position) {
        adapter.replaceData(tweet,position);
        tweetModelList = adapter.getData();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void setRecyclerView(){
        adapter = new TimelineComplexAdapter(this,presenter);
        adapter.setListener(new TimelineComplexAdapter.ItemClickListener() {
            @Override
            public void onItemClick(TweetModel item) {
                FragmentManager manager = getSupportFragmentManager();
            }
        });

        manager = new LinearLayoutManager(this);
        rvTimeline.setLayoutManager(manager);
        rvTimeline.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.start();
            }
        });
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        scrollListener = new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                presenter.loadmore();

            }
        };
        rvTimeline.addOnScrollListener(scrollListener);
    }

}
