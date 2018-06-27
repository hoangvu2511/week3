package com.example.soundloneteamcomp.twitterclient.adapter;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.soundloneteamcomp.twitterclient.R;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;
import com.volokh.danylo.hashtaghelper.HashTagHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.recyclerview.widget.RecyclerView;


public class TimelineComplexAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Tweet> listTweet ;
    Context ctx;
    ItemClickListener listener;
    private int lastPosition = -1;

    public TimelineComplexAdapter(Context context){
        this.listTweet = new ArrayList<>();
        this.ctx = context;
    }

    public void setData(List<Tweet> timelineItems){
        this.listTweet = timelineItems;
        notifyDataSetChanged();
    }

    public void setListener(ItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType){
            case 1:
                View viewImg = inflater.inflate(R.layout.timeline_item_with_img,parent,false);
                holder = new ViewHolderImg(viewImg);
                break;
            case 2:
                View viewNormal = inflater.inflate(R.layout.timeline_item,parent,false);
                holder = new ViewHolderNormal(viewNormal);
                break;
            default:
                View viewDefault = inflater.inflate(R.layout.timeline_item_with_imgs_alots,parent,false);
                holder = new ViewHolderImgsAlot(viewDefault);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        setAnimation(holder.itemView, position);
        switch (holder.getItemViewType()){
            case 1:
                ViewHolderImg imgView = (ViewHolderImg) holder;
                configureImg(imgView,position);
                break;
            case 2:
                ViewHolderNormal normalView = (ViewHolderNormal) holder;
                configureNormal(normalView,position);
                break;
            default:
                ViewHolderImgsAlot normalView2 = (ViewHolderImgsAlot) holder;
                configureImgs_Alots(normalView2, position);
                break;
        }
    }

    private void configureImgs_Alots(ViewHolderImgsAlot normalView2, int position) {
        Tweet tweet = listTweet.get(position);
        User user = tweet.user;

        normalView2.name.setText(user.name);
        String screenName = "@"+user.screenName;
        normalView2.userName.setText(screenName);
        normalView2.time.setText(getRelativeTimeAgo(tweet.createdAt));
        normalView2.content.setText(tweet.text);
        loadProfileImg(ctx,normalView2.imgV,user.profileImageUrlHttps);
        HashTagHelper hashTagHelper = HashTagHelper.Creator.create(R.color.tw__composer_blue_text,
                view -> {
                    Log.e("Clicked","has clicked");
                });
        hashTagHelper.handle(normalView2.content);

        if(tweet.retweetCount > 0)
            normalView2.btnRetweet.setText(String.valueOf(tweet.retweetCount));
        if (tweet.favoriteCount > 0)
            normalView2.btnLove.setText(String.valueOf(tweet.favoriteCount));

        loadImg(ctx,normalView2.imgContent,tweet.entities.media.get(0).mediaUrlHttps);
    }

    private void configureNormal(ViewHolderNormal normalView, int position) {
        Tweet tweet = listTweet.get(position);
        User user = tweet.user;

        normalView.name.setText(user.name);
        String screenName = "@"+user.screenName;
        normalView.userName.setText(screenName);

        normalView.content.setText(tweet.text);
        loadProfileImg(ctx,normalView.imgV,user.profileImageUrlHttps);
        HashTagHelper hashTagHelper = HashTagHelper.Creator.create(R.color.tw__composer_blue_text,
                view -> {
                Log.e("Clicked","has clicked");
            });
        hashTagHelper.handle(normalView.content);

        if(tweet.retweetCount > 0)
            normalView.btnRetweet.setText(String.valueOf(tweet.retweetCount));
        if (tweet.favoriteCount > 0)
            normalView.btnLove.setText(String.valueOf(tweet.favoriteCount));

        normalView.time.setText(getRelativeTimeAgo(tweet.createdAt));
    }

    private void configureImg(ViewHolderImg imgView, int position) {
        Tweet tweet = listTweet.get(position);
        User user = tweet.user;

        imgView.name.setText(user.name);
        String screenName = "@"+user.screenName;
        imgView.userName.setText(screenName);
        imgView.time.setText(getRelativeTimeAgo(tweet.createdAt));
        imgView.content.setText(tweet.text);

        if(tweet.retweetCount > 0)
            imgView.btnRetweet.setText(String.valueOf(tweet.retweetCount));
        if (tweet.favoriteCount > 0)
            imgView.btnLove.setText(String.valueOf(tweet.favoriteCount));

        HashTagHelper hashTagHelper = HashTagHelper.Creator.create(R.color.tw__composer_blue_text,
                view -> {
                    Log.e("Clicked","has clicked");
                });
        hashTagHelper.handle(imgView.content);
        loadImg(ctx,imgView.imgContent,tweet.entities.media.get(0).mediaUrlHttps);

        loadProfileImg(ctx,imgView.imgV,user.profileImageUrlHttps);
    }

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(ctx, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    private void loadImg(Context ctx,ImageView view, String url){
        Glide.with(ctx).load(url).into(view);
    }

    private void loadProfileImg(Context ctx,ImageView view, String url){
        Glide.with(ctx)
                .load(url)
                .apply(RequestOptions.circleCropTransform())
                .into(view);
    }

    @Override
    public int getItemCount() {
        return listTweet.size();
    }

    @Override
    public int getItemViewType(int position) {
        int numMedia = listTweet.get(position).entities.media.size();
        switch (numMedia){
            case 0:
                return 2;
            case 1:
                return 1;
            default:
                return 3;
        }
    }

    public interface ItemClickListener {
        void onItemClick(Tweet item);
    }

    private class ViewHolderImg extends RecyclerView.ViewHolder  implements ItemClickListener{
        ImageView imgV,imgContent;
        TextView name,userName,time,content;
        Button btnCmt,btnRetweet,btnLove,btnShare;
        View include;

        public ViewHolderImg(View viewImg) {
            super(viewImg);
            setId();
        }

        @Override
        public void onItemClick(Tweet item) {
            listener.onItemClick(listTweet.get(getAdapterPosition()));
        }
        private void setId(){
            imgV = itemView.findViewById(R.id.imageView);
            imgContent = itemView.findViewById(R.id.img_content);
            name = itemView.findViewById(R.id.name);
            userName = itemView.findViewById(R.id.screen_name);
            time = itemView.findViewById(R.id.time_tweet);
            content = itemView.findViewById(R.id.content);
            include = itemView.findViewById(R.id.include);

            btnCmt = include.findViewById(R.id.btnComment);
            btnRetweet = include.findViewById(R.id.btnRetweet);
            btnLove = include.findViewById(R.id.btnLove);
            btnShare = include.findViewById(R.id.btnShare);
        }
    }

    private class ViewHolderNormal extends RecyclerView.ViewHolder implements ItemClickListener{
        ImageView imgV;
        TextView name,userName,time,content;
        Button btnCmt,btnRetweet,btnLove,btnShare;
        View include;
        public ViewHolderNormal(View viewNormal) {
            super(viewNormal);
            setId();
        }

        @Override
        public void onItemClick(Tweet item) {
            listener.onItemClick(listTweet.get(getAdapterPosition()));
        }
        private void setId(){
            imgV = itemView.findViewById(R.id.imageView);
            name = itemView.findViewById(R.id.name);
            userName = itemView.findViewById(R.id.screen_name);
            time = itemView.findViewById(R.id.time_tweet);
            content = itemView.findViewById(R.id.content);
            include = itemView.findViewById(R.id.include);

            btnCmt = include.findViewById(R.id.btnComment);
            btnRetweet = include.findViewById(R.id.btnRetweet);
            btnLove = include.findViewById(R.id.btnLove);
            btnShare = include.findViewById(R.id.btnShare);
        }
    }

    private class ViewHolderImgsAlot extends RecyclerView.ViewHolder {
        ImageView imgV,imgContent;
        TextView name,userName,time,content;
        Button btnCmt,btnRetweet,btnLove,btnShare;
        View include;

        public ViewHolderImgsAlot(View viewDefault) {
            super(viewDefault);
            setId();
        }
        private void setId(){
            imgV = itemView.findViewById(R.id.imageView);
            imgContent = itemView.findViewById(R.id.img_content);
            name = itemView.findViewById(R.id.name);
            userName = itemView.findViewById(R.id.screen_name);
            time = itemView.findViewById(R.id.time_tweet);
            content = itemView.findViewById(R.id.content);

            include = itemView.findViewById(R.id.include);

            btnCmt = include.findViewById(R.id.btnComment);
            btnRetweet = include.findViewById(R.id.btnRetweet);
            btnLove = include.findViewById(R.id.btnLove);
            btnShare = include.findViewById(R.id.btnShare);
        }
    }

    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}
