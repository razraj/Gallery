package com.ramraj.work.model;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by ramraj on 29/6/17.
 */

public class CropResult {
    public final Bitmap debugImage;
    public final Bitmap resultImage;

    private CropResult( Bitmap debugImage, Bitmap resultImage) {
        this.debugImage = debugImage;
        this.resultImage = resultImage;
    }

    public static CropResult newInstance(Bitmap debugImage, Bitmap resultImage) {
        return new CropResult(debugImage, resultImage);
    }
}
