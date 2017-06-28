package com.ramraj.work.fragment;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ramraj.work.R;
import com.ramraj.work.base.BaseFragment;
import com.ramraj.work.db.StorageService;
import com.ramraj.work.model.Image;
import com.ramraj.work.widget.DragRectView;


import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ramraj on 28/6/17.
 */

public class EditImageFragment extends BaseFragment {

    private final String TAG =  EditImageFragment.class.getSimpleName();
    private String url;
    int x1, x2, y1, y2;

    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.view1)
    DragRectView view1;

    public void setUrl(String url) {
        this.url = url;
    }

    public EditImageFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_image, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Glide.with(this).load(url).into(image);
        final Image image = StorageService.getInstance().getLikedImage(url);
        if (image.getTop() != 0 && image.getLeft() != 0 && image.getBottom() != 0 && image.getRight() != 0) {
            Log.d(TAG+"reset", "left: "+image.getLeft()+" top:"+image.getTop()+" right:"+image.getRight()+" bottom:"+image.getBottom() );

            view1.setRectCoordinates(image.getLeft(), image.getTop(),image.getRight(),image.getBottom());
        }
        if (null != view1) {
            view1.setOnUpCallback(new DragRectView.OnUpCallback() {
                @Override
                public void onRectFinished(final Rect rect) {
                    x1 = rect.left;
                    y1 = rect.top;
                    x2 = rect.right;
                    y2 = rect.bottom;

                    Log.d(TAG, "left: "+rect.left+" top:"+rect.top+" right:"+rect.right+" bottom:"+rect.bottom );

                    image.setTop(y1);
                    image.setLeft(x1);
                    image.setBottom(y2);
                    image.setRight(x2);
                    StorageService.getInstance().store(image);
//                    Toast.makeText(getContext(), "Coordenadas (" + x1 + ", " + y1 + ", " + x2 + ", " + y2 + ")", Toast.LENGTH_LONG).show();
                }
            });
        }
    }


}
