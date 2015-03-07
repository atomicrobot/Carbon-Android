package com.mycompany.myapp.monitoring;

import com.mycompany.myapp.app.ForApplicationScope;

import dagger.Module;
import dagger.Provides;
import timber.log.Timber;
import timber.log.Timber.DebugTree;
import timber.log.Timber.Tree;

@Module
public class LoggerModule {
    private final Tree tree;

    public LoggerModule() {
        Timber.plant(new DebugTree());
        tree = Timber.asTree();
    }

    @ForApplicationScope
    @Provides
    Tree provideTimberTree() {
        return tree;
    }
}
