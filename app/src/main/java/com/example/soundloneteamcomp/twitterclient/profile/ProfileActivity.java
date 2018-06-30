package com.example.soundloneteamcomp.twitterclient.profile;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.soundloneteamcomp.twitterclient.R;
import com.example.soundloneteamcomp.twitterclient.adapter.TimelineComplexAdapter;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ProfileActivity extends AppCompatActivity implements ProfileCotract.View {
    RecyclerView rvReview;
    ImageView backgroundImg;
    Toolbar toolbar;
    ImageView fabLike;
    TimelineComplexAdapter adapter;

    ProfileCotract.Presenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setId();
        setUpRecyclerView();
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> {
            finish();
        });
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Animation animator = AnimationUtils.loadAnimation(this, R.anim.scale_anim);
        fabLike.startAnimation(animator);

        presenter = new ProfilePresenter(this, TwitterCore.getInstance().getSessionManager().getActiveSession());
        presenter.getTimeline(getIntent().getLongExtra("userId",1));
    }

    private void setId(){
        rvReview = findViewById(R.id.rvReview);
        backgroundImg = findViewById(R.id.ivCover);
        toolbar = findViewById(R.id.toolbar);
        fabLike = findViewById(R.id.fbLike);
    }

    private void setUpRecyclerView(){
        rvReview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        List<Tweet> listTweet = new ArrayList<>();
        adapter = new TimelineComplexAdapter(this,null);
        adapter.setData(listTweet);
        rvReview.setAdapter(adapter);
        loadImg(backgroundImg);
        loadProfile(fabLike);
    }


    @Override
    public void onGetStatusesSuccess(List<Tweet> data) {
        adapter.setData(data);
    }

    @Override
    public void showInfor(Tweet tweet) {

    }
    private void loadImg(ImageView imageView){
        Glide.with(this).load(getIntent().getStringExtra("backUrl"))
                .apply(RequestOptions.placeholderOf(R.drawable.ic_launcher_background)).into(imageView);
    }
    private void loadProfile(ImageView imageView){
        Glide.with(this).load(getIntent().getStringExtra("proUrl"))
                .apply(RequestOptions.circleCropTransform()).into(imageView);

    }

    @Override
    public void setPresenter(ProfileCotract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showLoading(boolean isShow) {

    }

    @Override
    public void showError(String message) {

    }
}
