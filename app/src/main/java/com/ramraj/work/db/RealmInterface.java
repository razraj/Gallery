package com.ramraj.work.db;

import com.ramraj.work.model.Image;

import java.util.ArrayList;

import io.reactivex.Observable;

/**
 * Created by ramraj on 28/6/17.
 */

public interface RealmInterface {
    Observable<ArrayList<Image>> getAllLikedImages();
}
