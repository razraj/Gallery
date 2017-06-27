package com.ramraj.work;

import android.app.Application;
import android.content.Context;

import com.ramraj.work.dagger.component.AppComponent;
import com.ramraj.work.dagger.component.DaggerAppComponent;
import com.ramraj.work.dagger.component.DaggerStorageComponent;
import com.ramraj.work.dagger.component.StorageComponent;
import com.ramraj.work.dagger.module.AppModule;
import com.ramraj.work.dagger.module.DbModule;
import com.ramraj.work.dagger.module.PrefModule;

/**
 * Created by ramraj on 27/6/17.
 */

public class App extends Application {
    private static AppComponent appComponent;
    private static StorageComponent storageComponent;

    public static Context getAppContext() {
        return appComponent.application().getApplicationContext();
    }

    public static StorageComponent getStorageComponent() {
        return storageComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
        storageComponent = DaggerStorageComponent.builder().appModule(new AppModule(this)).dbModule(new DbModule()).prefModule(new PrefModule()).build();

    }
}
