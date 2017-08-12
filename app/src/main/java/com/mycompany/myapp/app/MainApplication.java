package com.mycompany.myapp.app;

import android.app.Application;
import android.content.Intent;
import android.support.annotation.VisibleForTesting;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.security.ProviderInstaller;
import com.google.android.gms.security.ProviderInstaller.ProviderInstallListener;
import com.squareup.leakcanary.LeakCanary;

import javax.inject.Inject;

import timber.log.Timber;
import timber.log.Timber.Tree;

public class MainApplication extends Application implements HasComponent<ApplicationComponent> {
    private ApplicationComponent component;

    @Inject Tree logger;

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
        initApplicationComponent();
        Timber.plant(logger);

        initApplication();
        upgradeSecurityProvider();
    }

    private void initApplication() {
        new BuildConfigApplicationInitialization(this).immediateInitialization();
    }

    private void upgradeSecurityProvider() {
        ProviderInstaller.installIfNeededAsync(this, new ProviderInstallListener() {
            @Override
            public void onProviderInstalled() {

            }

            @Override
            public void onProviderInstallFailed(int errorCode, Intent recoveryIntent) {
                GoogleApiAvailability.getInstance().showErrorNotification(MainApplication.this, errorCode);
            }
        });
    }

    private void initApplicationComponent() {
        component = DaggerApplicationComponent.builder()
                .androidModule(new AndroidModule(this))
                .build();
        component.inject(this);
    }

    @Override
    public ApplicationComponent getComponent() {
        return component;
    }

    @VisibleForTesting
    public void setComponent(ApplicationComponent component) {
        this.component = component;
    }
}
