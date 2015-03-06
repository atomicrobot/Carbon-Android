package com.mycompany.myapp.modules;

import com.mycompany.myapp.monitoring.CrashReporter;
import com.mycompany.myapp.monitoring.CrashlyticsCrashReporter;

import dagger.Module;
import dagger.Provides;

@Module
public class CrashReporterModule {
    @Provides
    CrashReporter provideCrashReporter() {
        return new CrashlyticsCrashReporter();
    }
}
