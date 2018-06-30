package com.example.soundloneteamcomp.twitterclient.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.soundloneteamcomp.twitterclient.R;
import com.example.soundloneteamcomp.twitterclient.model.TweetModel;
import com.example.soundloneteamcomp.twitterclient.model.UserModel;
import com.example.soundloneteamcomp.twitterclient.profile.ProfileActivity;
import com.example.soundloneteamcomp.twitterclient.timeline.TimelineContract;
import com.example.soundloneteamcomp.twitterclient.util.ConvertTwitterHelper;
import com.google.android.material.button.MaterialButton;
import com.twitter.sdk.android.core.models.MediaEntity;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.internal.MultiTouchImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


public class TimelineComplexAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<TweetModel> listTweet ;
    Context ctx;
    ItemClickListener listener;
    TimelineContract.Presenter presenterTime;
    ConvertTwitterHelper convert;
    private int lastPosition = -1;

    boolean isRet = false,isLove = false;

    public TimelineComplexAdapter(Context context,@Nullable TimelineContract.Presenter presenter){
        this.listTweet = new ArrayList<>();
        this.convert = new ConvertTwitterHelper();
        this.ctx = context;
        this.presenterTime = presenter;
    }

    public void setData(List<Tweet> timelineItems){
        this.listTweet = convert.ConvertList(timelineItems);
        notifyDataSetChanged();
    }

    public void replaceData(Tweet tweet,int position){
        this.listTweet.set(position,convert.ConvertTweet(tweet));
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
            case 3:
                View viewVideo = inflater.inflate(R.layout.timeline_item_with_video,parent,false);
                holder = new ViewHolderVideo(viewVideo);
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
            case 3:
                ViewHolderVideo viewHolderVideo = (ViewHolderVideo) holder;
                configureVideo(viewHolderVideo,position);
                break;
            default:
                ViewHolderImgsAlot normalView2 = (ViewHolderImgsAlot) holder;
                configureImgs_Alots(normalView2, position);
                break;
        }
    }

    private void configureVideo(ViewHolderVideo viewHolderVideo, int position) {
        TweetModel tweet = listTweet.get(position);
        UserModel user = tweet.user;
        MediaEntity mediaEntity = tweet.extendedEntities.media.get(0);
        viewHolderVideo.name.setText(user.name);
        String screenName = "@"+user.screenName;
        viewHolderVideo.userName.setText(screenName);

        viewHolderVideo.content.setText(tweet.text);
        loadProfileImg(ctx,viewHolderVideo.imgV,user.profileImageUrlHttps);

        setlink(viewHolderVideo.content);
        setListener(viewHolderVideo.imgV,viewHolderVideo.btnCmt,viewHolderVideo.btnRetweet,viewHolderVideo.btnLove,viewHolderVideo.btnShare,tweet,position);

        if(tweet.retweetCount > 0)
            viewHolderVideo.btnRetweet.setText(String.valueOf(tweet.retweetCount));
        if (tweet.favoriteCount > 0)
            viewHolderVideo.btnLove.setText(String.valueOf(tweet.favoriteCount));

        viewHolderVideo.time.setText(getRelativeTimeAgo(tweet.createdAt));

        viewHolderVideo.videoView.setVideoPath(mediaEntity.videoInfo.variants.get(0).url);
        MediaController controller = new MediaController(ctx,true);
        viewHolderVideo.videoView.setMediaController(controller);
        viewHolderVideo.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
                controller.setAnchorView(viewHolderVideo.videoView);
                mediaPlayer.start();
            }
        });
        viewHolderVideo.videoView.setOnClickListener(view -> {
            if (controller.isShowing())
                controller.hide();
            else
                controller.show();
        });


        checkBtn(viewHolderVideo.btnRetweet, viewHolderVideo.btnLove,tweet);

    }

    private void configureImgs_Alots(ViewHolderImgsAlot normalView2, int position) {
        TweetModel tweet = listTweet.get(position);
        UserModel user = tweet.user;

        normalView2.name.setText(user.name);
        String screenName = "@"+user.screenName;
        normalView2.userName.setText(screenName);
        normalView2.time.setText(getRelativeTimeAgo(tweet.createdAt));
        normalView2.content.setText(tweet.text);
        setlink(normalView2.content);
        setListener( normalView2.imgV,normalView2.btnCmt,normalView2.btnRetweet,normalView2.btnLove,normalView2.btnShare,tweet,position);

        if(tweet.retweetCount > 0)
            normalView2.btnRetweet.setText(String.valueOf(tweet.retweetCount));
        if (tweet.favoriteCount > 0)
            normalView2.btnLove.setText(String.valueOf(tweet.favoriteCount));

        loadProfileImg(ctx,normalView2.imgV,user.profileImageUrlHttps);
        loadImg(ctx,normalView2.imgContent,tweet.entities.media.get(0).mediaUrlHttps);

        normalView2.imgContent.setOnClickListener(view -> {
            Log.e("Error","imgSSSSSS");

        });
        checkBtn(normalView2.btnRetweet, normalView2.btnLove,tweet);

//        for (MediaEntity media : tweet.entities.media){
//            loadImg(ctx,normalView2.touchImageView,media.mediaUrlHttps);
//        }
    }

    private void configureNormal(ViewHolderNormal normalView, int position) {
        TweetModel tweet = listTweet.get(position);
        UserModel user = tweet.user;

        normalView.name.setText(user.name);
        String screenName = "@"+user.screenName;
        normalView.userName.setText(screenName);

        normalView.content.setText(tweet.text);
        loadProfileImg(ctx,normalView.imgV,user.profileImageUrlHttps);

        setlink(normalView.content);
        setListener(normalView.imgV,normalView.btnCmt,normalView.btnRetweet,normalView.btnLove,normalView.btnShare,tweet,position);

        if(tweet.retweetCount > 0)
            normalView.btnRetweet.setText(String.valueOf(tweet.retweetCount));
        if (tweet.favoriteCount > 0)
            normalView.btnLove.setText(String.valueOf(tweet.favoriteCount));

        normalView.time.setText(getRelativeTimeAgo(tweet.createdAt));
        checkBtn(normalView.btnRetweet, normalView.btnLove,tweet);
    }

    private void configureImg(ViewHolderImg imgView, int position) {
        TweetModel tweet = listTweet.get(position);
        UserModel user = tweet.user;

        imgView.name.setText(user.name);
        String screenName = "@"+user.screenName;
        imgView.userName.setText(screenName);
        imgView.time.setText(getRelativeTimeAgo(tweet.createdAt));
        imgView.content.setText(tweet.text);

        setlink(imgView.content);
        setListener(imgView.imgV ,imgView.btnCmt,imgView.btnRetweet,imgView.btnLove,imgView.btnShare,tweet,position);

        imgView.imgContent.setOnClickListener(view -> {
            Log.e("Error","img");

        });

        if(tweet.retweetCount > 0)
            imgView.btnRetweet.setText(String.valueOf(tweet.retweetCount));
        if (tweet.favoriteCount > 0)
            imgView.btnLove.setText(String.valueOf(tweet.favoriteCount));


        loadImg(ctx,imgView.imgContent,tweet.entities.media.get(0).mediaUrlHttps);

        loadProfileImg(ctx,imgView.imgV,user.profileImageUrlHttps);
        checkBtn(imgView.btnRetweet, imgView.btnLove,tweet);
    }




    private void setlink(TextView view){
        new PatternEditableBuilder().
                addPattern(Pattern.compile("\\@(\\w+)"), Color.BLUE,
                        new PatternEditableBuilder.SpannableClickedListener() {
                            @Override
                            public void onSpanClicked(String text) {
                                Toast.makeText(ctx, "Clicked username: " + text,
                                        Toast.LENGTH_SHORT).show();
                            }
                        }).into(view);
        new PatternEditableBuilder().
                addPattern(Pattern.compile("\\#(\\w+)"), Color.BLUE,
                        new PatternEditableBuilder.SpannableClickedListener() {
                            @Override
                            public void onSpanClicked(String text) {
                                Toast.makeText(ctx, "Clicked username: " + text,
                                        Toast.LENGTH_SHORT).show();
                            }
                        }).into(view);
    }

    private void setAnimation(View viewToAnimate, int position) {
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

    private void setListener(ImageView img,MaterialButton cmt, MaterialButton retweet,
                             MaterialButton love, MaterialButton share, TweetModel tweet,int position){

        img.setOnClickListener(view->{
            Toast.makeText(ctx,"click img",Toast.LENGTH_SHORT).show();

            Intent inent = new Intent(ctx,ProfileActivity.class);
            inent.putExtra("userId",tweet.user.id);
            inent.putExtra("proUrl",tweet.user.profileImageUrlHttps);
            inent.putExtra("backUrl",tweet.user.profileBackgroundImageUrlHttps);
            ctx.startActivity(inent);
        });

        cmt.setOnClickListener(view -> {
            Toast.makeText(ctx,"click cmt",Toast.LENGTH_SHORT).show();
        });

        retweet.setOnClickListener(view->{
            String a = retweet.getText().toString();
            if(!a.equals(""))
                if (!isRet){
                    isRet = true;
                    presenterTime.updateRetweet(tweet.id,position);
                }
                else{
                    isRet = false;
                    presenterTime.updateUndoRetweet(tweet.id,position);
                }
            else {
                isRet = !isRet;
                presenterTime.updateRetweet(tweet.id,position);
            }
        });

        love.setOnClickListener(view -> {
            String a = love.getText().toString();
            if (a!= "")
                if (!isLove){
                    isLove = true;
                    presenterTime.updateFav(tweet.id,position);
                }
                else{
                    isLove = false;
                    presenterTime.updateUndoFav(tweet.id,position);
                }
            else {
                isLove = !isLove;
                presenterTime.updateFav(tweet.id,position);
            }
        });

        share.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, "Share to");
            intent.setType("text/plain");
            ctx.startActivity(Intent.createChooser(intent, "Send To"));

        });

    }

    private void checkBtn(MaterialButton retweet, MaterialButton love,TweetModel tweet){
        if (tweet.favorited){
            isLove = true;
            Drawable icon = ContextCompat.getDrawable(ctx,R.drawable.heart_clicked);
            love.setIcon(icon);
            love.setIconTint(ContextCompat.getColorStateList(ctx,R.color.colorRed));
        }
        if (tweet.retweeted){
            isRet = true;
            Drawable icon = ContextCompat.getDrawable(ctx,R.drawable.ic_action_retweet_clicked);
            retweet.setIcon(icon);
            retweet.setIconTint(ContextCompat.getColorStateList(ctx,R.color.colorGreen));
        }
    }

    @Override
    public int getItemCount() {
        return listTweet.size();
    }

    @Override
    public int getItemViewType(int position) {
        TweetModel model = listTweet.get(position);
        int numEntity = model.entities.media.size();
        int numExtendEntity = model.extendedEntities.media.size();
//        switch (numMedia){
//            case 0:
//                return 2;
//            case 1:
//                MediaEntity media = listTweet.get(position).extendedEntities.media.get(0);
//                String type = media.type;
//                if (type.equals("video"))
//                    return 3;
//                return 1;
//            default:
//                return 4;
//        }
        if (numEntity == 0)
            return 2;
        else if(numEntity==numExtendEntity && numEntity==1 && model.extendedEntities.media.get(0).type.equals("photo"))
            return 1;
        else if (model.extendedEntities.media.get(0).type.equals("video") && numEntity == 1)
            return 3;
        return 4;

    }

    public interface ItemClickListener {
        void onItemClick(TweetModel item);
    }

    private class ViewHolderImg extends RecyclerView.ViewHolder  implements ItemClickListener{
        ImageView imgV,imgContent;
        TextView name,userName,time,content;
        MaterialButton btnCmt,btnRetweet,btnLove,btnShare;
        View include;

        public ViewHolderImg(View viewImg) {
            super(viewImg);
            setId();
        }

        @Override
        public void onItemClick(TweetModel item) {
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
        MaterialButton btnCmt,btnRetweet,btnLove,btnShare;
        View include;
        public ViewHolderNormal(View viewNormal) {
            super(viewNormal);
            setId();
        }

        @Override
        public void onItemClick(TweetModel item) {
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
        MaterialButton btnCmt,btnRetweet,btnLove,btnShare;
        View include;
        MultiTouchImageView touchImageView;

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

            touchImageView = itemView.findViewById(R.id.multiImage);
        }
    }

    private class ViewHolderVideo extends RecyclerView.ViewHolder implements ItemClickListener{
        ImageView imgV;
        TextView name,userName,time,content;
        MaterialButton btnCmt,btnRetweet,btnLove,btnShare;
        View include;
        VideoView videoView;
        public ViewHolderVideo(View itemView) {
            super(itemView);
            setId();
        }

        @Override
        public void onItemClick(TweetModel item) {
            listener.onItemClick(listTweet.get(getAdapterPosition()));
        }
        private void setId(){
            imgV = itemView.findViewById(R.id.imageView);
            name = itemView.findViewById(R.id.name);
            userName = itemView.findViewById(R.id.screen_name);
            time = itemView.findViewById(R.id.time_tweet);
            content = itemView.findViewById(R.id.content);

            videoView = itemView.findViewById(R.id.xvideos_com);

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


    public void getPostion(){

    }
}
