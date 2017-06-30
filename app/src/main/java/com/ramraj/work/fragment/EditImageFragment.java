package com.ramraj.work.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.ramraj.work.R;
import com.ramraj.work.SmartCrop;
import com.ramraj.work.base.BaseFragment;
import com.ramraj.work.db.StorageService;
import com.ramraj.work.model.CropResult;
import com.ramraj.work.model.Image;
import com.ramraj.work.model.Options;
import com.ramraj.work.widget.OverlayImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ramraj on 28/6/17.
 */

public class EditImageFragment extends BaseFragment {

    private final String TAG = EditImageFragment.class.getSimpleName();
    private String url;
    int x1 = 0, x2 = 0, y1 = 300, y2 = 300;
    private Image originalImage;

    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.croped_image)
    ImageView croppedImage;
    @BindView(R.id.overlay_view)
    OverlayImageView overlayImageView;

    private int imageWidth, imageHeight;

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
        Glide.with(this).load(url).into(image).getSize(new SizeReadyCallback() {
            @Override
            public void onSizeReady(int width, int height) {
                imageWidth = width;
                imageHeight = height;
            }
        });

        originalImage = StorageService.getInstance().getLikedImage(url);
        if (overlayImageView != null) {
            overlayImageView.setOnUpCallback(new OverlayImageView.OnUpCallback() {
                @Override
                public void onRectFinished(Rect rect) {
                    x1 = rect.left;
                    y1 = rect.top;
                    x2 = rect.right;
                    y2 = rect.bottom;
                    originalImage.setTop(y1);
                    originalImage.setLeft(x1);
                    originalImage.setBottom(y2);
                    originalImage.setRight(x2);
                    StorageService.getInstance().store(originalImage);
                }
            });
        }


    }

    @OnClick(R.id.crop_now)
    void onCropClick() {
        cropImage();
    }

    private void cropImage() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        int widthError = imageWidth - width;
        int heightError = imageHeight - height;
        Uri myUri = Uri.parse(originalImage.getUrl());
        try {
            Bitmap selectedImage = BitmapFactory.decodeStream(new FileInputStream(new File(myUri.getPath())));
            selectedImage = getResizedBitmap(selectedImage, image.getWidth(), image.getHeight());
//            selectedImage = Bitmap.createScaledBitmap(selectedImage, image.getWidth(),image.getHeight() , true);
            SmartCrop.analyzeWithObservable(Options.getInstance().cropSquareSize(widthError, heightError), selectedImage
                    , new Rect(x1, y1, x2, y2))
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<CropResult>() {
                        @Override
                        public void accept(@NonNull CropResult cropResult) throws Exception {
                            croppedImage.setImageBitmap(cropResult.resultImage);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@NonNull Throwable throwable) throws Exception {
                            Log.d(TAG, "accept: something went wrong"+throwable);
                        }
                    });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);
        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }
}
