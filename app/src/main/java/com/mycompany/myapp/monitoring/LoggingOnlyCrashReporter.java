package com.mycompany.myapp.monitoring;

import android.util.Log;

public class LoggingOnlyCrashReporter implements CrashReporter {
    private static final String TAG = LoggingOnlyCrashReporter.class.getSimpleName();

    @Override
    public void logMessage(String message) {
        Log.i(TAG, message);
    }

    @Override
    public void logException(String message, Exception ex) {
        Log.e(TAG, message, ex);
    }
}
