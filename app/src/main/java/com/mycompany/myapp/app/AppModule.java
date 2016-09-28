package com.mycompany.myapp.app;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    @Provides
    public Settings provideSettings(Context context) {
        return new Settings(context);
    }
}
