package com.ramraj.work.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ramraj.work.R;
import com.ramraj.work.db.StorageService;
import com.ramraj.work.model.Image;
import com.ramraj.work.utils.ColorGenerator;
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

    public GalleryAdapter(Context context) {
        this.context = context;
    }

    public void setImages(ArrayList<Image> images) {
        this.images = images;
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
                Glide.with(context).load(R.drawable.circular_view).centerCrop().into(holder.like_thumbnail);
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
                Glide.with(context).load(R.drawable.circular_view).centerCrop().into(holder.like_thumbnail);
        } else
            holder.like_thumbnail.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    class GalleryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.thumbnail)
        ImageView thumbnail;

        @BindView(R.id.like_thumbnail)
        ImageView like_thumbnail;

        public GalleryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.like_thumbnail)
        void onLikeClick() {
            if (!images.get(getAdapterPosition()).isLiked()) {
                images.get(getAdapterPosition()).setLiked(true);
                StorageService.getInstance().store(images.get(getAdapterPosition()));
                notifyItemChanged(getAdapterPosition(), images.get(getAdapterPosition()));
            } else if (StorageService.getInstance().delete(images.get(getAdapterPosition()).getUrl())) {
                images.get(getAdapterPosition()).setLiked(false);
                notifyItemChanged(getAdapterPosition(), images.get(getAdapterPosition()));
            } else
                Utils.showToast("Something went wrong");
        }

        @OnLongClick(R.id.thumbnail)
        boolean onLikeLongClick() {
            if (!isEditable)
                isEditable = true;
            else
                isEditable = false;

            notifyDataSetChanged();
            return true;
        }
    }
}
