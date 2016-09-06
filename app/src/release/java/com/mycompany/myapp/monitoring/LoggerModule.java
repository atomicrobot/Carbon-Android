package com.mycompany.myapp.monitoring;

import com.mycompany.myapp.monitoring.model.NoOpTree;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import timber.log.Timber.Tree;

@Module
public class LoggerModule {
    @Singleton
    @Provides
    public Tree provideTimberTree() {
        return new NoOpTree();
    }
}
