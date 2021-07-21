package com.atomicrobot.carbon.modules

import com.atomicrobot.carbon.monitoring.CrashlyticsCrashReporter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
@InstallIn(Singleton::class)
class CrashReporterModule {
    @Singleton
    @Provides
    fun provideCrashReporter() = CrashlyticsCrashReporter()
}
