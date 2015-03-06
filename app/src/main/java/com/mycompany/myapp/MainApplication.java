package com.mycompany.myapp;

import android.app.Application;

import com.mycompany.myapp.monitoring.CrashReporter;

import javax.inject.Inject;

import hugo.weaving.DebugLog;

public class MainApplication extends Application {

    private ApplicationComponent applicationComponent;

    @Inject
    CrashReporter crashReporter;

    @DebugLog
    @Override
    public void onCreate() {
        super.onCreate();

        applicationComponent = ApplicationComponent.Initializer.init(this);
        applicationComponent.inject(this);

        crashReporter.startCrashReporter();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
