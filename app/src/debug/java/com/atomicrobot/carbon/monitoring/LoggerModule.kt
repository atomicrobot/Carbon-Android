package com.atomicrobot.carbon.monitoring

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import timber.log.Timber
import timber.log.Timber.DebugTree
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LoggerModule {
    @Singleton
    @Provides
    fun provideTimberTree() : Timber.Tree = DebugTree()
}
