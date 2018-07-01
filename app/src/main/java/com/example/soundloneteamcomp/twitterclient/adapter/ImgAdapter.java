package com.example.soundloneteamcomp.twitterclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.soundloneteamcomp.twitterclient.R;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class ImgAdapter extends RecyclerView.Adapter<ImgAdapter.ViewHolderImg> {
    List<String> url;
    Context ctx;

    public ImgAdapter(Context context){
        this.ctx = context;
        url = new ArrayList<>();
    }

    public void setUrl(List<String> urls){
        this.url = urls;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolderImg onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_img,parent,false);
        return new ViewHolderImg(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderImg holder, int position) {
        loadImg(ctx,holder.img,url.get(position));
    }

    private void loadImg(Context ctx,ImageView view, String url){
        Glide.with(ctx)
                .load(url)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .into(view);
    }

    @Override
    public int getItemCount() {
        return url.size();
    }

    public class ViewHolderImg extends RecyclerView.ViewHolder {
        ImageView img;
        public ViewHolderImg(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.item_img);
        }
    }
}
