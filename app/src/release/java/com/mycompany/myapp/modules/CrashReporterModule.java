package com.mycompany.myapp.modules;

import com.mycompany.myapp.monitoring.CrashReporter;
import com.mycompany.myapp.monitoring.CrashlyticsCrashReporter;

import dagger.Module;
import dagger.Provides;

@Module
public class CrashReporterModule {
    private final CrashReporter crashReporter;

    public CrashReporterModule() {
        this.crashReporter = new CrashlyticsCrashReporter();
    }

    @DebugLog
    @Provides
    CrashReporter provideCrashReporter() {
        return crashReporter
    }
}
