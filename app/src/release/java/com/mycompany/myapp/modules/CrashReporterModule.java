package com.mycompany.myapp.modules;

import com.mycompany.myapp.monitoring.CrashReporter;
import com.mycompany.myapp.monitoring.CrashlyticsCrashReporter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class CrashReporterModule {
    @Singleton
    @Provides
    CrashReporter provideCrashReporter() {
        return new CrashlyticsCrashReporter();
    }
}
