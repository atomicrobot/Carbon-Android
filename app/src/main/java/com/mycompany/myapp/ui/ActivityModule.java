package com.mycompany.myapp.ui;

import android.app.Activity;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {
    private final Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    @ForActivityScope
    Activity provideActivity() {
        return activity;
    }
}
