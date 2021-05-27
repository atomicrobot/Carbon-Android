package com.atomicrobot.carbon.app

import android.app.Application
import android.content.Context
import com.atomicrobot.carbon.app.MainApplicationInitializer
import dagger.Module
import dagger.Provides
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
    @Named("loading_delay_ms")
    fun loadingDelayMs() = loadingDelayMs

    companion object {
        private const val LOADING_DELAY_MS: Long = 500
    }
}
