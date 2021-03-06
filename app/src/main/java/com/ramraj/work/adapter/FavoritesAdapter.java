package com.ramraj.work.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ramraj.work.Events;
import com.ramraj.work.R;
import com.ramraj.work.model.Image;
import com.ramraj.work.utils.ColorGenerator;
import com.ramraj.work.utils.RxBus;
import com.ramraj.work.widget.TextDrawable;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ramraj on 28/6/17.
 */

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder> {

    private ArrayList<Image> images = new ArrayList<>();
    private Context context;

    public FavoritesAdapter(Context context) {
        this.context = context;
    }

    public void setImages(ArrayList<Image> images){
        this.images = images;
        notifyDataSetChanged();
    }

    @Override
    public FavoritesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FavoritesViewHolder(LayoutInflater.from(context).inflate(R.layout.favorites_image_card, parent, false));
    }

    @Override
    public void onBindViewHolder(FavoritesViewHolder holder, int position) {
        Image image = images.get(position);
        if (image.getUrl() != null)
            Glide.with(context).load(image.getUrl()).centerCrop().into(holder.thumbnail);
        else {
            TextDrawable drawable = TextDrawable.builder().buildRect((position + 1) + "", ColorGenerator.MATERIAL.getColor(position % ColorGenerator.MATERIAL.getSize()));
            holder.thumbnail.setImageDrawable(drawable);
        }
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    class FavoritesViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.favorites_thumbnail)
        ImageView thumbnail;

        public FavoritesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.favorites_thumbnail)
        void onImageClick(){
            RxBus.getInstance().sendEvent(new Events.ImageSelectedEvent(images.get(getAdapterPosition())));
        }
    }
}
