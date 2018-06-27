package com.example.soundloneteamcomp.twitterclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.soundloneteamcomp.twitterclient.R;
import com.example.soundloneteamcomp.twitterclient.model.TimelineItem;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.ViewHolderTimeline> {
    List<Tweet> list ;
    Context ctx;
    ItemClickListener listener;

    public TimelineAdapter(Context context){
        this.list = new ArrayList<>();
        this.ctx = context;
    }

    public void setData(List<Tweet> timelineItems){
        this.list = timelineItems;
        notifyDataSetChanged();
    }

    public void setListener(ItemClickListener listener){
        this.listener = listener;
    }


    @Override
    public ViewHolderTimeline onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.timeline_item,parent,false);
        return new ViewHolderTimeline(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderTimeline holder, int position) {
        Tweet tweet = list.get(position);
        User user = tweet.user;

        holder.name.setText(user.name);
        String screenName = "@"+user.screenName;
        holder.userName.setText(screenName);
        holder.time.setText(tweet.createdAt);
        holder.content.setText(tweet.text);
        Glide.with(ctx)
                .applyDefaultRequestOptions(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .placeholder(R.drawable.ic_profile_img)
                        .override(150,150)
                        .circleCrop())
                .load(user.profileImageUrlHttps)
                .into(holder.imgV);
        if (-1 > 0)
            holder.btnCmt.setText(String.valueOf(tweet.inReplyToStatusId));
        if(tweet.retweetCount > 0)
            holder.btnRetweet.setText(String.valueOf(tweet.retweetCount));
        if (tweet.favoriteCount > 0)
            holder.btnLove.setText(String.valueOf(tweet.favoriteCount));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolderTimeline extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imgV;
        TextView name,userName,time,content;
        Button btnCmt,btnRetweet,btnLove,btnShare;
        View include;
        public ViewHolderTimeline(View itemView) {
            super(itemView);
            setId();
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

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
    public interface ItemClickListener {
        void onItemClick(TimelineItem item);
    }

}
