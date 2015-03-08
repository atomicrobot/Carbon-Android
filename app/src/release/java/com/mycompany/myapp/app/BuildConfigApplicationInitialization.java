package com.mycompany.myapp.app;

import com.crashlytics.android.Crashlytics;

public class BuildConfigApplicationInitialization {
    private final MainApplication application;

    public BuildConfigApplicationInitialization(MainApplication application) {
        this.application = application;
    }

    public void immediateInitialization() {
        Crashlytics.start(application);
    }
}
