package com.atomicrobot.carbon.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel(
    app: Application,
    protected val disposables: CompositeDisposable = CompositeDisposable()
) : AndroidViewModel(app) {

    var viewModelInitialized: Boolean = false

    fun initializeViewModel() {
        if (!viewModelInitialized) {
            viewModelInitialized = true
            setupViewModel()
        }
    }

    protected abstract fun setupViewModel()

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }
}
