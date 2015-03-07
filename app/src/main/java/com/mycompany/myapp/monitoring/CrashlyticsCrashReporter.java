package com.mycompany.myapp.monitoring;

import com.crashlytics.android.Crashlytics;

public class CrashlyticsCrashReporter implements CrashReporter {
    @Override
    public void logMessage(String message) {
        Crashlytics.log(message);
    }

    @Override
    public void logException(String message, Exception ex) {
        Crashlytics.log(message);
        Crashlytics.logException(ex);
    }
}
