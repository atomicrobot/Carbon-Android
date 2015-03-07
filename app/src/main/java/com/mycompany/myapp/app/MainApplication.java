package com.mycompany.myapp.app;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.mycompany.myapp.BuildConfig;

import javax.inject.Inject;

import hugo.weaving.DebugLog;
import timber.log.Timber.Tree;

public class MainApplication extends Application implements HasComponent<ApplicationComponent> {
    private ApplicationComponent component;

    @Inject
    @ForApplicationScope
    Tree logger;

    @DebugLog
    @Override
    public void onCreate() {
        super.onCreate();

        startCrashReporter();

        component = Dagger_ApplicationComponent.builder()
                .androidModule(new AndroidModule(this))
                .build();
        component.inject(this);

        logger.i("Application is ready to go!");
    }

    /**
     * We would normally not expose our implementation details like this, but the first thing we
     * want to do is actually get the crash reporter going before anything else.
     */
    private void startCrashReporter() {
        if (!BuildConfig.DEBUG) {
            Crashlytics.start(this);
        }
    }

    @Override
    public ApplicationComponent getComponent() {
        return component;
    }
}
