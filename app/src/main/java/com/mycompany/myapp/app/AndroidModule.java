package com.mycompany.myapp.app;

import android.app.Application;

import dagger.Module;
import dagger.Provides;
import hugo.weaving.DebugLog;

@Module
public class AndroidModule {
    private final MainApplication application;

    @DebugLog
    public AndroidModule(MainApplication application) {
        this.application = application;
    }

    @Provides
    public Application application() {
        return application;
    }
}
