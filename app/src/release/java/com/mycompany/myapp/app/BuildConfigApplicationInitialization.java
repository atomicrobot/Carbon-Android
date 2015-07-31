package com.mycompany.myapp.app;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

public class BuildConfigApplicationInitialization {
    private final MainApplication application;

    public BuildConfigApplicationInitialization(MainApplication application) {
        this.application = application;
    }

    public void immediateInitialization() {
        Fabric.with(application, new Crashlytics());
    }
}
