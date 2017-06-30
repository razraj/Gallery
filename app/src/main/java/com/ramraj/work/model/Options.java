package com.ramraj.work.model;

import android.graphics.Bitmap;

/**
 * Created by ramraj on 29/6/17.
 */

public enum Options {
    INSTANCE;
    private int cropWidth = 100;
    private int cropHeight = 100;
    private int widthError = 0;
    private int heightError = 0;
    protected Bitmap.Config bitmapConfig = Bitmap.Config.ARGB_8888;

    public static Options getInstance(){
        return INSTANCE;
    }

    Options() {
    }

    public Options cropSquareSize(int widthError,int heightError ) {
        this.widthError = widthError;
        this.heightError = heightError;
        return this;
    }

    public int getCropWidth() {
        return cropWidth;
    }

    public int getCropHeight() {
        return cropHeight;
    }

    public int getWidthError() {
        return widthError;
    }

    public int getHeightError() {
        return heightError;
    }

    public Bitmap.Config getBitmapConfig() {
        return bitmapConfig;
    }
}
