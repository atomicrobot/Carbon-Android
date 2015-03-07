package com.mycompany.myapp.ui;

import android.app.Activity;

import dagger.Module;
import dagger.Provides;
import hugo.weaving.DebugLog;

@Module
public class ActivityModule {
    private final Activity activity;

    @DebugLog
    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    @ForActivityScope
    Activity provideActivity() {
        return activity;
    }
}
