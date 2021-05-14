package com.atomicrobot.carbon.monitoring

import dagger.Module
import dagger.Provides
import timber.log.Timber
import timber.log.Timber.DebugTree
import javax.inject.Singleton

@Module
class LoggerModule {
    @Singleton
    @Provides
    fun provideTimberTree() : Timber.Tree = DebugTree()
}
