package com.ramraj.work.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by ramraj on 27/6/17.
 */

public class BaseFragment extends Fragment {

    protected CompositeDisposable networkDisposable;
    protected CompositeDisposable rxBusEventDisposable;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        networkDisposable = new CompositeDisposable();
        rxBusEventDisposable = new CompositeDisposable();
    }

    @Override
    public void onStop() {
        super.onStop();
        networkDisposable.clear();
        rxBusEventDisposable.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        networkDisposable.clear();
        networkDisposable = null;
        rxBusEventDisposable.clear();
        rxBusEventDisposable = null;
    }
}
