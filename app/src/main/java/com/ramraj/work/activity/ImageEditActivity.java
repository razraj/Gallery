package com.ramraj.work.activity;

import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ramraj.work.R;
import com.ramraj.work.cropper.CropOverlayView;
import com.ramraj.work.cropper.photoview.IGetImageBounds;
import com.ramraj.work.cropper.photoview.PhotoView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageEditActivity extends AppCompatActivity {

    @BindView(R.id.image)
    PhotoView imageView;

    @BindView(R.id.crop_overlay)
    CropOverlayView cropOverlayView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_edit);
        ButterKnife.bind(this);
        String image = getIntent().getStringExtra("image_url");
        Glide.with(this).load(image).into(imageView);

        imageView.setImageBoundsListener(new IGetImageBounds() {
            @Override
            public Rect getImageBounds() {
                Rect rect = cropOverlayView.getImageBounds();
                return rect;
            }
        });
    }
}
