package com.atomicrobot.carbon.util

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import java.util.concurrent.TimeUnit

object RxUtils {
    fun <T> delayAtLeast(observable: Observable<T>, delayMs: Long): Observable<T> {
        val timer = Observable.timer(delayMs, TimeUnit.MILLISECONDS)
        return Observable.combineLatest<Long, T, T>(timer, observable, BiFunction { _, response -> response })
    }
}
