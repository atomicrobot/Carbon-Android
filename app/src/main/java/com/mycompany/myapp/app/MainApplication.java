package com.mycompany.myapp.app;

import android.app.Application;
import android.support.annotation.VisibleForTesting;

import javax.inject.Inject;

public class MainApplication extends Application {
    private ApplicationComponent component;

    @Inject MainApplicationInitializer initializer;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerApplicationComponent.builder()
                .androidModule(new AndroidModule(this))
                .build();
        component.inject(this);

        initializeApplication();
    }

    /**
     * For Espresso tests we *do not* want to initialize the full application and instead will
     * favor mocking out non-ui dependencies. This should be overridden to a no-op if initialization
     * should not occur.
     */
    @VisibleForTesting
    protected void initializeApplication() {
        initializer.initialize();
    }

    public ApplicationComponent getComponent() {
        return component;
    }

    @VisibleForTesting
    public void setComponent(ApplicationComponent component) {
        this.component = component;
    }
}
