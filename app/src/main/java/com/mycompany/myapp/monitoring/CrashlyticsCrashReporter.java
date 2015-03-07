package com.mycompany.myapp.monitoring;

import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.mycompany.myapp.app.ForApplicationScope;

import javax.inject.Inject;

public class CrashlyticsCrashReporter implements CrashReporter {
    @Inject
    @ForApplicationScope
    Context context;

    @Override
    public void startCrashReporter() {
        Crashlytics.start(context);
    }

    @Override
    public void logMessage(String message) {
        Crashlytics.log(message);
    }
}
