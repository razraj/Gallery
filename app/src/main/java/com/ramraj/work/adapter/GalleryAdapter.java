package com.ramraj.work.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ramraj.work.Events;
import com.ramraj.work.R;
import com.ramraj.work.db.StorageService;
import com.ramraj.work.model.Image;
import com.ramraj.work.utils.ColorGenerator;
import com.ramraj.work.utils.RxBus;
import com.ramraj.work.utils.Utils;
import com.ramraj.work.widget.TextDrawable;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

/**
 * Created by ramraj on 27/6/17.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {
    private Context context;
    private ArrayList<Image> images = new ArrayList<>();
    public boolean isEditable = false;
    private ArrayList<Image> likedImages = new ArrayList<>();

    public GalleryAdapter(Context context) {
        this.context = context;
    }

    public void setImages(ArrayList<Image> images) {
        this.images = images;
        notifyDataSetChanged();
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
        notifyDataSetChanged();
    }

    @Override
    public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GalleryViewHolder(LayoutInflater.from(context).inflate(R.layout.gallery_image_card, parent, false));
    }

    @Override
    public void onBindViewHolder(GalleryViewHolder holder, int position, List<Object> payloads) {
        if (payloads != null && !payloads.isEmpty() && (payloads.get(0) instanceof Image)) {
            Image image = (Image) payloads.get(0);
            // update the specific view
            if (image.isLiked())
                Glide.with(context).load(R.drawable.checked).centerCrop().into(holder.like_thumbnail);
            else
                Glide.with(context).load(R.drawable.success).centerCrop().into(holder.like_thumbnail);
        } else {
            // I have already overridden  the other onBindViewHolder(ViewHolder, int)
            // The method with 3 arguments is being called before the method with 2 args.
            // so calling super will call that method with 2 arguments.
            super.onBindViewHolder(holder, position, payloads);
        }
    }

    @Override
    public void onBindViewHolder(GalleryViewHolder holder, int position) {
        Image image = images.get(position);
        if (image.getUrl() != null)
            Glide.with(context).load(image.getUrl()).centerCrop().into(holder.thumbnail);
        else {
            TextDrawable drawable = TextDrawable.builder().buildRect((position + 1) + "", ColorGenerator.MATERIAL.getColor(position % ColorGenerator.MATERIAL.getSize()));
            holder.thumbnail.setImageDrawable(drawable);
        }
        if (isEditable) {
            holder.like_thumbnail.setVisibility(View.VISIBLE);
            if (image.isLiked())
                Glide.with(context).load(R.drawable.checked).centerCrop().into(holder.like_thumbnail);
            else
                Glide.with(context).load(R.drawable.success).centerCrop().into(holder.like_thumbnail);
        } else
            holder.like_thumbnail.setVisibility(View.GONE);
    }

    public int getLikedImages() {
        int liked=0;
        for (int i = 0; i < images.size(); i++) {
            if(images.get(i).isLiked())
                liked++;
        }
        return liked;
    }

    public void toogleSelection(int position) {
        if (!images.get(position).isLiked()) {
            images.get(position).setLiked(true);
            likedImages.add(images.get(position));
            StorageService.getInstance().store(images.get(position));
        } else if (StorageService.getInstance().delete(images.get(position).getUrl())) {
            images.get(position).setLiked(false);
            likedImages.remove(images.get(position));
        } else
            Utils.showToast("Something went wrong");
//        setEditable(true);
        notifyItemChanged(position, images.get(position));
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class GalleryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.thumbnail)
        ImageView thumbnail;

        @BindView(R.id.like_thumbnail)
        ImageView like_thumbnail;

        public GalleryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.thumbnail)
        void onLikeClick() {
            RxBus.getInstance().sendEvent(new Events.OnItemClicked(getAdapterPosition()));
        }

        @OnLongClick(R.id.thumbnail)
        boolean onLikeLongClick() {
            if (!isEditable) {
                setEditable(true);
                Log.d("GalleryAdapter", "onLikeLongClick: " + this.getPosition());
                RxBus.getInstance().sendEvent(new Events.OnItemLongClicked(this.getPosition()));
            }
            return true;
        }
    }
}
