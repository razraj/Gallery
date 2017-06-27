package com.ramraj.work.db;

import android.content.SharedPreferences;

import com.ramraj.work.App;
import com.ramraj.work.model.Image;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by ramraj on 27/6/17.
 */

public enum StorageService implements RealmInterface {
    INSTANCE;

    @Inject
    RealmConfiguration realmConfiguration;

    @Inject
    SharedPreferences sharedPreferences;

    public static StorageService getInstance() {
        return INSTANCE;
    }

    StorageService() {
        App.getStorageComponent().inject(this);
    }

    public Realm getRealmInstance() {
        return Realm.getInstance(realmConfiguration);
    }

    public <T extends RealmObject> T store(T model) {
        Realm realm = getRealmInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(model);
        realm.commitTransaction();
        realm.close();
        return model;
    }

    public void delete(Class<? extends RealmModel> clazz) {
        Realm realm = getRealmInstance();
        realm.beginTransaction();
        realm.delete(clazz);
        realm.commitTransaction();
        realm.close();

    }

    public boolean delete(String image_url) {
        Realm realm = getRealmInstance();
        realm.beginTransaction();
        RealmResults<Image> rows = realm.where(Image.class).equalTo("url", image_url).findAll();
        boolean value = rows.deleteAllFromRealm();
        realm.commitTransaction();
        realm.close();
        return value;
    }

    public <T extends RealmObject> List<T> store(List<T> model) {
        Realm realm = getRealmInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(model);
        realm.commitTransaction();
        realm.close();
        return model;
    }

    public Image getLikedImage(String image_url) {
        Realm realm = StorageService.getInstance().getRealmInstance();
        Image image = realm.where(Image.class).equalTo("url", image_url).findFirst();
        if (image != null)
            image = realm.copyFromRealm(image);
        realm.close();
        return image;
    }


    @Override
    public Observable<ArrayList<Image>> getAllLikedImages() {
        Realm realm = StorageService.getInstance().getRealmInstance();
        ArrayList<Image> likedImages = new ArrayList(realm.where(Image.class).findAll());
        realm.close();
        return Observable.just(likedImages);
    }
}
