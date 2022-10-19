package com.atomicrobot.carbon

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.stopKoin

class RxTests {
    private lateinit var subscriber: TestObserver<String>

    @Before
    fun setup() {
        subscriber = TestObserver()
    }

    @After
    fun teardown() {
        stopKoin()
    }

    @Test
    fun testObservableFactoryError() {
        Observable.error<String>(IllegalStateException())
            .subscribe(subscriber)
        subscriber.assertError(IllegalStateException::class.java)
    }

    @Test
    fun testSingleFactoryError() {
        Single.error<String>(IllegalStateException())
            .subscribe(subscriber)
        subscriber.assertError(IllegalStateException::class.java)
    }

    @Test
    fun testCompletableFactoryError() {
        Completable.error(IllegalStateException())
            .subscribe(subscriber)
        subscriber.assertError(IllegalStateException::class.java)
    }

    @Test
    fun testObservableCallableError() {
        Observable.fromCallable { throw IllegalStateException() }
            .subscribe(subscriber)
        subscriber.assertError(IllegalStateException::class.java)
    }

    @Test
    fun testSingleCallableError() {
        Single.fromCallable { throw IllegalStateException() }
            .subscribe(subscriber)
        subscriber.assertError(IllegalStateException::class.java)
    }

    @Test
    fun testCompletableCallableError() {
        Completable.fromCallable { throw IllegalStateException() }
            .subscribe(subscriber)
        subscriber.assertError(IllegalStateException::class.java)
    }
}
