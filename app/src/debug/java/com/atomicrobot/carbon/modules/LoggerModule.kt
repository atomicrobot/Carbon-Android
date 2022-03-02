package com.atomicrobot.carbon.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import timber.log.Timber

@InstallIn(SingletonComponent::class)
@Module
object LoggerModule {
    @Provides
    fun provideTimberTree(): Timber.Tree = Timber.DebugTree()
}