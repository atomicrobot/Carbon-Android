package com.mycompany.myapp.monitoring;

import android.util.Log;

public class LoggingOnlyCrashReporter implements CrashReporter {
    private static final String TAG = LoggingOnlyCrashReporter.class.getSimpleName();

    @Override
    public void startCrashReporter() {
        Log.v(TAG, "Starting the logging only crash reporter.");
    }

    @Override
    public void logMessage(String message) {
        Log.e(TAG, message);
    }
}
