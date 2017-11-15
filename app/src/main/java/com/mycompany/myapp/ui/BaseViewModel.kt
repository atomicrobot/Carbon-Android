package com.mycompany.myapp.ui

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.databinding.Observable
import android.databinding.PropertyChangeRegistry
import android.os.Bundle
import android.os.Parcelable
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel<State : Parcelable>(
        app: Application,
        private val stateKey: String,
        protected var state: State,
        protected val disposables: CompositeDisposable = CompositeDisposable())
    : AndroidViewModel(app), Observable {

    @Transient private var databindingCallbacks: PropertyChangeRegistry? = null

    fun saveState(bundle: Bundle) {
        bundle.putParcelable(stateKey, state)
    }

    fun restoreState(bundle: Bundle?) {
        if (bundle != null && bundle.containsKey(stateKey)) {
            state = bundle.getParcelable(stateKey)
        }
    }

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback) {
        synchronized(this) {
            if (databindingCallbacks == null) {
                databindingCallbacks = PropertyChangeRegistry()
            }
        }

        databindingCallbacks!!.add(callback)
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback) {
        synchronized(this) {
            if (databindingCallbacks == null) {
                return
            }
        }

        databindingCallbacks!!.remove(callback)
    }

    fun notifyChange() {
        synchronized(this) {
            if (databindingCallbacks == null) {
                return
            }
        }

        databindingCallbacks!!.notifyCallbacks(this, 0, null)
    }

    fun notifyPropertyChanged(fieldId: Int) {
        synchronized(this) {
            if (databindingCallbacks == null) {
                return
            }
        }

        databindingCallbacks!!.notifyCallbacks(this, fieldId, null)
    }
}