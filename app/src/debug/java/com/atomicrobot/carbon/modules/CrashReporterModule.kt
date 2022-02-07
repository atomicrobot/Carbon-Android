package com.atomicrobot.carbon.modules

import com.atomicrobot.carbon.monitoring.CrashReporter
import com.atomicrobot.carbon.monitoring.LoggingOnlyCrashReporter
import dagger.Binds
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
abstract class CrashReporterModule {

    @Singleton
    @Binds
    abstract fun bindLoggingOnlyCrashReporter(impl: LoggingOnlyCrashReporter): CrashReporter
}
