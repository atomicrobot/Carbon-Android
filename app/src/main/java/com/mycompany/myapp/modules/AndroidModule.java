package com.mycompany.myapp.modules;

import android.content.Context;

import com.mycompany.myapp.MainApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AndroidModule {
    private final MainApplication application;

    public AndroidModule(MainApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    @ForApplication
    public Context provideApplicationContext() {
        return application;
    }
}
