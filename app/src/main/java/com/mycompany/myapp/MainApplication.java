package com.mycompany.myapp;

import android.app.Application;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import hugo.weaving.DebugLog;

public class MainApplication extends Application {
    private static final String TAG = MainApplication.class.getSimpleName();

    @DebugLog
    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            setupDebugConfiguration();
        } else {
            setupReleaseConfiguration();
        }

        setupReleaseConfiguration();
    }

    private void setupDebugConfiguration() {
        Log.i(TAG, "Starting with the debug configuration.");
    }

    private void setupReleaseConfiguration() {
        Crashlytics.start(this);
    }
}
