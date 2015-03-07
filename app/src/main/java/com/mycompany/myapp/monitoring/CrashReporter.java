package com.mycompany.myapp.monitoring;

public interface CrashReporter {
    public void startCrashReporter();

    public void logMessage(String message);
}
