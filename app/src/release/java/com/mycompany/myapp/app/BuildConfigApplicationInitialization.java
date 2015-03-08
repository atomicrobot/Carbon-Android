package com.mycompany.myapp.app;

import com.crashlytics.android.Crashlytics;

public class ApplicationInitialization {
    private final MainApplication application;

    public ApplicationInitialization(MainApplication application) {
        this.application = application;
    }

    public void immediateInitialization() {
        Crashlytics.start(application);
    }
}
