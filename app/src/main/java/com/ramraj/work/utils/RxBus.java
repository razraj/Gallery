package com.ramraj.work.utils;

import android.support.annotation.NonNull;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by ramraj on 28/6/17.
 */

public class RxBus {
    private static RxBus mInstance;
    private Subject publisher = PublishSubject.create();

    public static RxBus getInstance() {
        if (mInstance == null) {
            mInstance = new RxBus();
        }

        return mInstance;
    }

    public void sendEvent(@NonNull Object object) {
        if(publisher.hasObservers()) {
            publisher.onNext(object);
        }
    }

    public Observable<Object> toObservable() {
        return publisher;
    }

}
