package com.mycompany.myapp.util

import io.reactivex.disposables.CompositeDisposable

object RxUtils {
    fun getSafeCompositeDisposable(disposable: CompositeDisposable?): CompositeDisposable {
        return when {
            disposable == null || disposable.isDisposed -> CompositeDisposable()
            else -> disposable
        }
    }
}