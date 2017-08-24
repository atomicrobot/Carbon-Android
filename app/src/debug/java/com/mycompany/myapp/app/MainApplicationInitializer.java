package com.mycompany.myapp.app;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;

import com.mycompany.myapp.ui.RiseAndShine;

import timber.log.Timber.Tree;

/**
 * Specific to the debug variant.
 */
public class MainApplicationInitializer extends BaseApplicationInitializer {
    public MainApplicationInitializer(Application application, Tree logger) {
        super(application, logger);
    }

    @Override
    public void initialize() {
        super.initialize();
        application.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                RiseAndShine.riseAndShine(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }
}
