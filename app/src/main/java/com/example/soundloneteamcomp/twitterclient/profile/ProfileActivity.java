package com.example.soundloneteamcomp.twitterclient.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.soundloneteamcomp.twitterclient.R;
import com.example.soundloneteamcomp.twitterclient.adapter.TimelineComplexAdapter;
import com.example.soundloneteamcomp.twitterclient.compose.ComposeTweetActivity;
import com.example.soundloneteamcomp.twitterclient.model.TweetModel;
import com.example.soundloneteamcomp.twitterclient.scrollListener.EndlessRecyclerViewScrollListener;
import com.example.soundloneteamcomp.twitterclient.util.ConvertTwitterHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
    FloatingActionButton comp;
    TimelineComplexAdapter adapter;
    TextView textView;
    ProfileCotract.Presenter presenter;

    LinearLayoutManager manager;
    EndlessRecyclerViewScrollListener scrollListener ;

    long id;

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
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        id = getIntent().getLongExtra("userId",1);
        Animation animator = AnimationUtils.loadAnimation(this, R.anim.scale_anim);
        fabLike.startAnimation(animator);

        presenter = new ProfilePresenter(this, TwitterCore.getInstance().getSessionManager().getActiveSession());
        presenter.getTimeline(id);
    }

    private void setId(){
        rvReview = findViewById(R.id.rvReview);
        backgroundImg = findViewById(R.id.ivCover);
        toolbar = findViewById(R.id.toolbar);
        fabLike = findViewById(R.id.fbLike);
        comp = findViewById(R.id.composeBtn);
        comp.setOnClickListener(view -> {
            startActivity(new Intent(this, ComposeTweetActivity.class));
            overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right );
        });
        textView = findViewById(R.id.name_user);
    }

    private void setUpRecyclerView(){
        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvReview.setLayoutManager(manager);

        scrollListener = new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                presenter.loadMore(id);
            }

            @Override
            public void onScrolled(RecyclerView view, int dx, int dy) {
                super.onScrolled(view, dx, dy);
                if (dy>0)
                    comp.hide();
                else comp.show();
            }
        };

        List<Tweet> listTweet = new ArrayList<>();
        adapter = new TimelineComplexAdapter(this,null);
        adapter.setData(new ConvertTwitterHelper().ConvertList(listTweet));
        rvReview.setAdapter(adapter);
        rvReview.addOnScrollListener(scrollListener);
        loadImg(backgroundImg);
        loadProfile(fabLike);
    }


    @Override
    public void onGetStatusesSuccess(List<TweetModel> data) {
        adapter.setData(data);
        if (id==1){
            Glide.with(this).load(data.get(0).user.profileImageUrlHttps)
                    .apply(RequestOptions.circleCropTransform()).into(fabLike);
        }
//        textView.setText(data.get(0).user.name);
    }

    @Override
    public void showInfor(String name) {
        textView.setText(name);
    }

    @Override
    public void onUpdateTweet(TweetModel data,int position) {
        adapter.replaceData(data,position);
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
