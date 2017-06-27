package com.ramraj.work.dagger.component;

import android.app.Application;

import com.ramraj.work.dagger.module.AppModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by ramraj on 27/6/17.
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    Application application();
}
