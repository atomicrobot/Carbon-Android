package com.mycompany.myapp.monitoring;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import timber.log.Timber;
import timber.log.Timber.DebugTree;
import timber.log.Timber.Tree;

@Module
public class LoggerModule {
    @Singleton
    @Provides
    Tree provideTimberTree() {
        Timber.plant(new DebugTree());
        return Timber.asTree();
    }
}
