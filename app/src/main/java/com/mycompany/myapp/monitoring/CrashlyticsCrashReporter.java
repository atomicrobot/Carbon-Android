package com.mycompany.myapp.monitoring;

import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.mycompany.myapp.modules.ForApplication;

import javax.inject.Inject;

public class CrashlyticsCrashReporter implements CrashReporter {
    @Inject
    @ForApplication
    Context context;

    @Override
    public void startCrashReporter() {
        Crashlytics.start(context);
    }
}
