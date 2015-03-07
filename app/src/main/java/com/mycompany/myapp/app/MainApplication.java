package com.mycompany.myapp.app;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.mycompany.myapp.BuildConfig;

import hugo.weaving.DebugLog;

public class MainApplication extends Application {

    private ApplicationComponent applicationComponent;

    @DebugLog
    @Override
    public void onCreate() {
        super.onCreate();

        startCrashReporter();

        applicationComponent = Dagger_ApplicationComponent.builder()
                .androidModule(new AndroidModule(this))
                .build();
        applicationComponent.inject(this);
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

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
