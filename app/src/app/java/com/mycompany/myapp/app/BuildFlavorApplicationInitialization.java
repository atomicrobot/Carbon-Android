package com.mycompany.myapp.app;

import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;

import com.mycompany.myapp.ui.RiseAndShine;

public class BuildFlavorApplicationInitialization {
    private final MainApplication application;

    public BuildFlavorApplicationInitialization(MainApplication application) {
        this.application = application;
    }

    public void immediateInitialization() {

    }
}
