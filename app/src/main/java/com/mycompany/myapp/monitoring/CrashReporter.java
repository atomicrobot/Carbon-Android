package com.mycompany.myapp.monitoring;

public interface CrashReporter {
    public void logMessage(String message);
    public void logException(String message, Exception ex);
}
