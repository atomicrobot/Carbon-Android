package com.mycompany.myapp.ui

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.databinding.BaseObservable
import android.os.Bundle
import android.os.Parcelable
import android.support.annotation.CallSuper
import com.mycompany.myapp.util.RxUtils
import io.reactivex.disposables.CompositeDisposable

abstract class BasePresenter<ViewContract : Any, State : Parcelable>(
        private val stateKey: String,
        protected var state: State)
    : BaseObservable(), LifecycleObserver {

    lateinit var view: ViewContract
    protected var disposables: CompositeDisposable = CompositeDisposable()

    fun saveState(bundle: Bundle) {
        bundle.putParcelable(stateKey, state)
    }

    fun restoreState(bundle: Bundle?) {
        if (bundle != null && bundle.containsKey(stateKey)) {
            state = bundle.getParcelable(stateKey)
            notifyChange()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    @CallSuper
    open fun onResume() {
        disposables = RxUtils.getNewCompositeDisposableIfDisposed(disposables)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    @CallSuper
    open fun onPause() {
        RxUtils.disposeIfNotNull(disposables)
    }
}
