package com.mycompany.myapp.app;

import android.app.Application;

import javax.inject.Inject;

import timber.log.Timber.Tree;

public class MainApplication extends Application implements HasComponent<ApplicationComponent> {
    @Inject
    Tree logger;

    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        new ApplicationInitialization(this).immediateInitialization();

        component = Dagger_ApplicationComponent.builder()
                .androidModule(new AndroidModule(this))
                .build();
        component.inject(this);
    }

    @Override
    public ApplicationComponent getComponent() {
        return component;
    }
}
