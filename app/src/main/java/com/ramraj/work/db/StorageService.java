package com.ramraj.work.db;

import android.content.SharedPreferences;

import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmModel;
import io.realm.RealmObject;

/**
 * Created by ramraj on 27/6/17.
 */

public enum StorageService {
    INSTANCE;

    @Inject
    RealmConfiguration realmConfiguration;

    @Inject
    SharedPreferences sharedPreferences;

    public static StorageService StorageService(Realm realm) {
        return INSTANCE;
    }

    StorageService() {
    }

    public Realm getRealmInstance() {
        return Realm.getInstance(realmConfiguration);
    }

    public <T extends RealmObject> T store(T model) {
        Realm realm = getRealmInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(model);
        realm.commitTransaction();
        return model;
    }

    public void delete(Class<? extends RealmModel> clazz) {
        Realm realm = getRealmInstance();
        realm.beginTransaction();
        realm.delete(clazz);
        realm.commitTransaction();

    }

    public <T extends RealmObject> List<T> store(List<T> model) {
        Realm realm = getRealmInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(model);
        realm.commitTransaction();
        return model;
    }
}
