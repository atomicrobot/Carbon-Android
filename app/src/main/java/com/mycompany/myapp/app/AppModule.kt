package com.mycompany.myapp.app

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber.Tree
import javax.inject.Named

@Module
class AppModule(private val loadingDelayMs: Long = LOADING_DELAY_MS) {

    @Provides
    fun provideMainApplicationInitializer(application: Application, logger: Tree): MainApplicationInitializer {
        return MainApplicationInitializer(application, logger)
    }

    @Provides
    fun provideSettings(context: Context) = Settings(context)

    @Provides
    @Named("io")
    fun ioScheduler() = Schedulers.io()

    @Provides
    @Named("main")
    fun mainThreadScheduler() = AndroidSchedulers.mainThread()

    @Provides
    @Named("loading_delay_ms")
    fun loadingDelayMs() = loadingDelayMs

    companion object {
        private const val LOADING_DELAY_MS: Long = 500
    }
}
