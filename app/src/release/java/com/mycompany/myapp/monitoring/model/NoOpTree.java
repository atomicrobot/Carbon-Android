package com.mycompany.myapp.monitoring.model;

import timber.log.Timber;

public class NoOpTree extends Timber.Tree {
    @Override
    protected void log(int priority, String tag, String message, Throwable t) {

    }

    @Override
    protected boolean isLoggable(String tag, int priority) {
        return false;
    }
}
