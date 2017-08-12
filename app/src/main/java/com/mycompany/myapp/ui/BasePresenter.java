package com.mycompany.myapp.ui;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mycompany.myapp.util.RxUtils;

import org.parceler.Parcels;

import io.reactivex.disposables.CompositeDisposable;

public abstract class BasePresenter<ViewContract, State> implements LifecycleObserver {
    private final String stateKey;

    protected CompositeDisposable disposables;
    protected ViewContract view;
    protected State state;

    public BasePresenter(String stateKey) {
        this.stateKey = stateKey;
    }

    public void setView(ViewContract viewContract) {
        this.view = viewContract;
    }

    public State getState() {
        return state;
    }

    public void saveState(@NonNull Bundle bundle) {
        bundle.putParcelable(stateKey, Parcels.wrap(state));
    }

    public void restoreState(@Nullable Bundle bundle) {
        if (bundle != null && bundle.containsKey(stateKey)) {
            state = Parcels.unwrap(bundle.getParcelable(stateKey));
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    @CallSuper
    public void onResume() {
        disposables = RxUtils.getNewCompositeDisposableIfDisposed(disposables);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    @CallSuper
    public void onPause() {
        RxUtils.disposeIfNotNull(disposables);
    }
}
