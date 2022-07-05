package com.atomicrobot.carbon.app

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier

@Qualifier
annotation class LoadingDelayMs

@InstallIn(SingletonComponent::class)
@Module
class AppModule(private val loadingDelayMs: Long = LOADING_DELAY_MS) {

    @Provides
    fun provideSettings(
        @ApplicationContext context: Context
    ): Settings {
        return Settings(context)
    }

    @LoadingDelayMs
    @Provides
    fun provideLoadingDelay(): Long = loadingDelayMs

    companion object {
        private const val LOADING_DELAY_MS: Long = 500
    }
}
