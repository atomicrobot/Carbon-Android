package com.mycompany.myapp.app;

import android.app.Application;
import android.content.Context;
import android.support.annotation.VisibleForTesting;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber.Tree;

@Module
public class AppModule {
    private static final long LOADING_DELAY_MS = 500;

    private final long loadingDelayMs;

    public AppModule() {
        this(LOADING_DELAY_MS);
    }

    @VisibleForTesting
    public AppModule(long loadingDelayMs) {
        this.loadingDelayMs = loadingDelayMs;
    }

    @Provides
    public MainApplicationInitializer provideMainApplicationInitializer(
            Application application,
            Tree logger) {
        return new MainApplicationInitializer(application, logger);
    }

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

    @Provides
    @Named("loading_delay_ms")
    public long loadingDelayMs() {
        return loadingDelayMs;
    }
}
