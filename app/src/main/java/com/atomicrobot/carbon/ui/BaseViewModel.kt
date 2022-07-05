package com.atomicrobot.carbon.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel

abstract class BaseViewModel(
    app: Application,
) : AndroidViewModel(app) {

    private var viewModelInitialized: Boolean = false

    fun initializeViewModel() {
        if (!viewModelInitialized) {
            viewModelInitialized = true
            setupViewModel()
        }
    }

    protected abstract fun setupViewModel()
}