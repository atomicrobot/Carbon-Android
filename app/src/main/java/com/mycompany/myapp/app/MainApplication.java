package com.mycompany.myapp.app;

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

        applicationComponent = Dagger_ApplicationComponent.builder()
                .androidModule(new AndroidModule(this))
                .build();
        applicationComponent.inject(this);

        crashReporter.startCrashReporter();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
