package com.mycompany.myapp.monitoring;

public interface CrashReporter {
    void logMessage(String message);
    void logException(String message, Exception ex);
}
