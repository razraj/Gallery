package com.ramraj.work.model;

import android.graphics.Rect;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ramraj on 27/6/17.
 */

public class Image extends RealmObject {
    @PrimaryKey
    private String url;
    private String name;
    private boolean liked = false;
    private boolean isEdited = false;

    private int top;
    private int left;
    private int bottom;
    private int right;

    public Image() {
    }

    public boolean isEdited() {
        return isEdited;
    }

    public void setEdited(boolean edited) {
        isEdited = edited;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getBottom() {
        return bottom;
    }

    public void setBottom(int bottom) {
        this.bottom = bottom;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Image) {
            Image temp = (Image) obj;
            if (this.url.equals(temp.getUrl()))
                return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return url.hashCode();
    }
}
