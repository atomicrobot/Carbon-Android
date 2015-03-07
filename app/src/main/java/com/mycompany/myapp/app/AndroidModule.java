package com.mycompany.myapp.app;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AndroidModule {
    private final MainApplication application;

    public AndroidModule(MainApplication application) {
        this.application = application;
    }

    @Singleton
    @Provides
    public Application application() {
        return application;
    }
}
