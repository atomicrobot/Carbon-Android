package com.mycompany.myapp.app;

import android.content.Context;

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
    @ForApplicationScope
    public MainApplication application() {
        return application;
    }

    @Provides
    @ForApplicationScope
    public Context applicationContext() {
        return application;
    }
}
