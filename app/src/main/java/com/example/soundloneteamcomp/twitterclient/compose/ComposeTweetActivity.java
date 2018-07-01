package com.example.soundloneteamcomp.twitterclient.compose;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.soundloneteamcomp.twitterclient.R;
import com.google.android.material.textfield.TextInputEditText;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ComposeTweetActivity extends AppCompatActivity implements ComposeContract.View{
    Button btnSend;
    Button btnCancel;
    TextInputEditText edtCompose;
    ProgressBar loader;
    ComposeContract.Presenter presenter;
    ImageView img;
    TextView name,screenName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        btnSend = findViewById(R.id.btnSend);
        btnCancel = findViewById(R.id.cancelBtn);
        edtCompose = findViewById(R.id.edtCompose);
        loader = findViewById(R.id.loader);
        img = findViewById(R.id.profileImg);
        name = findViewById(R.id.name);
        screenName = findViewById(R.id.screen_name);
        presenter = new ComposeTweetPresenter(this, TwitterCore.getInstance().getSessionManager().getActiveSession());

        btnSend.setOnClickListener( view -> presenter.sendTweet(edtCompose.getText().toString()));

        btnCancel.setOnClickListener(view -> {
            finish();
            overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        });
        presenter.getUrl();

    }

    private void loadProfile(ImageView img,String url){
        Glide.with(this)
                .load(url)
                .apply(RequestOptions
                        .circleCropTransform()
                        .placeholder(R.drawable.ic_profile_img))
                .into(img);
    }

    @Override
    public void setPresenter(ComposeContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showLoading(boolean isShow) {
        loader.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sendTweetSuccess(Result<Tweet> result) {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
        finish();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    @Override
    public void setUrl(User user) {
        loadProfile(img,user.profileImageUrlHttps);
        name.setText(user.name);
        screenName.setText("@"+user.screenName);
    }

}
