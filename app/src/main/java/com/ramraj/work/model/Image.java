package com.ramraj.work.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ramraj on 27/6/17.
 */

public class Image extends RealmObject{
    @PrimaryKey
    private String url;
    private String name;
    private boolean liked=false;

    public Image() {
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
