package com.mycompany.myapp.modules

import com.mycompany.myapp.monitoring.CrashlyticsCrashReporter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CrashReporterModule {
    @Singleton
    @Provides
    fun provideCrashReporter() = CrashlyticsCrashReporter()
}
