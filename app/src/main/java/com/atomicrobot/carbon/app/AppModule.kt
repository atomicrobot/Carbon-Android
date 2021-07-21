package com.atomicrobot.carbon.app

import android.app.Application
import android.content.Context
import com.atomicrobot.carbon.app.MainApplicationInitializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import timber.log.Timber.Tree
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule(private val loadingDelayMs: Long = LOADING_DELAY_MS) {

    @Provides
    @Singleton
    fun provideMainApplicationInitializer(application: MainApplication, logger: Tree): MainApplicationInitializer {
        return MainApplicationInitializer(application, logger)
    }

    @Provides
    @Singleton
    fun provideSettings(context: Context) = Settings(context)

    @Provides
    @Singleton
    @Named("loading_delay_ms")
    fun loadingDelayMs() = loadingDelayMs

    companion object {
        private const val LOADING_DELAY_MS: Long = 500
    }
}
