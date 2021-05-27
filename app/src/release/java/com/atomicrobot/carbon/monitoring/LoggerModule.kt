package com.atomicrobot.carbon.monitoring

import com.atomicrobot.carbon.monitoring.model.NoOpTree
import dagger.Module
import dagger.Provides
import timber.log.Timber
import javax.inject.Singleton

@Module
class LoggerModule {
    @Singleton
    @Provides
    fun provideTimberTree() : Timber.Tree = NoOpTree()
}
