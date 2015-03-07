package com.mycompany.myapp.modules;

import com.mycompany.myapp.monitoring.CrashReporter;
import com.mycompany.myapp.monitoring.CrashlyticsCrashReporter;

import dagger.Module;
import dagger.Provides;

@Module
public class CrashReporterModule {
    private final CrashReporter crashReporter;

    @DebugLog
    public CrashReporterModule() {
        this.crashReporter = new CrashlyticsCrashReporter();
    }

    @Provides
    CrashReporter provideCrashReporter() {
        return crashReporter;
    }
}
