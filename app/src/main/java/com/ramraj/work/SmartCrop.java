package com.ramraj.work;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import com.ramraj.work.model.Crop;
import com.ramraj.work.model.CropResult;
import com.ramraj.work.model.Options;

import org.reactivestreams.Subscriber;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

/**
 * Created by ramraj on 29/6/17.
 */

public class SmartCrop {
    private Options options;

    public SmartCrop(Options options) {
        this.options = options;
    }

    public static Observable<CropResult> analyzeWithObservable(final Options options, final Bitmap input,final Rect rect) {
        return Observable
                .create(new ObservableOnSubscribe<CropResult>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<CropResult> e) throws Exception {
                        e.onNext(SmartCrop.analyze(options, input,rect));
                        e.onComplete();
                    }
                });
    }

    private static CropResult analyze(Options options, Bitmap input,Rect rect) {
        return new SmartCrop(options).analyze(input,rect);
    }

    private CropResult analyze(Bitmap input,Rect rect) {
        Bitmap output = Bitmap.createBitmap(input.getWidth(), input.getHeight(), options.getBitmapConfig());
        Crop crop = new Crop(rect.left, rect.top,rect.right, rect.bottom);
        return CropResult.newInstance(output, createCrop(input, crop));
    }

    public Bitmap createCrop(Bitmap input, Crop crop) {
        int tw = crop.width-crop.x;
        int th = crop.width-crop.x;
        Bitmap image = Bitmap.createBitmap(tw, th, options.getBitmapConfig());
        new Canvas(image).drawBitmap(input, new Rect(crop.x, crop.y, crop.width, crop.height), new Rect(0, 0, tw, th), null);
        return image;
    }

}
