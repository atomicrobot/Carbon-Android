package com.mycompany.myapp.ui

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer

/**
 * A SingleLiveEvent used for Snackbar messages. Like a [SingleLiveEvent] but also prevents
 * null messages and uses a custom observer.
 *
 * Note that only one observer is going to be notified of changes.
 */
class SimpleSnackbarMessage : SingleLiveEvent<String>() {
    fun observe(owner: LifecycleOwner, observer: SnackbarObserver) {
        super.observe(owner, Observer { message ->
            if (message == null) {
                return@Observer
            }
            observer.onNewMessage(message)
        })
    }

    interface SnackbarObserver {
        /**
         * Called when there is a new message to be shown.
         */
        fun onNewMessage(message: String)
    }
}