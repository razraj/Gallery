package com.ramraj.work.dagger.component;

import android.content.SharedPreferences;

import com.ramraj.work.dagger.module.AppModule;
import com.ramraj.work.dagger.module.DbModule;
import com.ramraj.work.dagger.module.PrefModule;
import com.ramraj.work.db.PrefService;
import com.ramraj.work.db.StorageService;

import javax.inject.Singleton;

import dagger.Component;
import io.realm.RealmConfiguration;

/**
 * Created by ramraj on 27/6/17.
 */
@Singleton
@Component(modules = {DbModule.class, AppModule.class, PrefModule.class})
public interface StorageComponent {
    SharedPreferences sharedPreferences();

    RealmConfiguration realmConfiguration();

    void inject(StorageService storageService);

    void inject(PrefService prefService);

}
