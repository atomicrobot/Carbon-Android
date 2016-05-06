package com.mycompany.myapp.monitoring;

import timber.log.Timber;

public class LoggingOnlyCrashReporter implements CrashReporter {
    @Override
    public void logMessage(String message) {
        Timber.i(message);
    }

    @Override
    public void logException(String message, Exception ex) {
        Timber.e(ex, message);
    }
}
