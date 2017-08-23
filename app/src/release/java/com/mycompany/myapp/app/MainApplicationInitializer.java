package com.mycompany.myapp.app;

import android.app.Application;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber.Tree;

/**
 * Specific to the production variant.
 */
public class MainApplicationInitializer extends BaseApplicationInitializer {
    public MainApplicationInitializer(Application application, Tree logger) {
        super(application, logger);
    }

    @Override
    public void initialize() {
        super.initialize();

        Fabric.with(application, new Crashlytics());
    }
}
