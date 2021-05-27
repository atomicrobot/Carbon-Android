package com.atomicrobot.carbon.ui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

/**
 * A SingleLiveEvent used for Navigation events. Like a [SingleLiveEvent] but also prevents
 * null messages and uses a custom observer.
 *
 * Note that only one observer is going to be notified of changes.
 */
class NavigationEvent<T> : SingleLiveEvent<T>() {
    fun observe(owner: LifecycleOwner, observer: NavigationObserver<T>) {
        super.observe(owner, Observer { event ->
            if (event == null) {
                return@Observer
            }
            observer.onNavigationEvent(event)
        })
    }

    interface NavigationObserver<in T> {
        /**
         * Called when there is a new navigation event.
         */
        fun onNavigationEvent(event: T)
    }
}
