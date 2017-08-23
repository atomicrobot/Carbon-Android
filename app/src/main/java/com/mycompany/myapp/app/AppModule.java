package com.mycompany.myapp.app;

import android.content.Context;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Module
public class AppModule {
    @Provides
    public Settings provideSettings(Context context) {
        return new Settings(context);
    }

    @Provides
    @Named("io")
    public Scheduler ioScheduler() {
        return Schedulers.io();
    }

    @Provides
    @Named("main")
    public Scheduler mainThreadScheduler() {
        return AndroidSchedulers.mainThread();
    }
}
