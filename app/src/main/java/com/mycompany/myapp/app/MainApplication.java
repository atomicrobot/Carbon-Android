package com.mycompany.myapp.app;

import android.app.Application;
import android.content.Intent;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.security.ProviderInstaller;
import com.google.android.gms.security.ProviderInstaller.ProviderInstallListener;

import javax.inject.Inject;

import timber.log.Timber.Tree;

public class MainApplication extends Application implements HasComponent<ApplicationComponent> {
    @Inject
    Tree logger;

    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        initApplication();
        upgradeSecurityProvider();
        initApplicationComponent();
    }

    private void initApplication() {
        new BuildConfigApplicationInitialization(this).immediateInitialization();
        new BuildFlavorApplicationInitialization(this).immediateInitialization();
    }

    private void upgradeSecurityProvider() {
        ProviderInstaller.installIfNeededAsync(this, new ProviderInstallListener() {
            @Override
            public void onProviderInstalled() {

            }

            @Override
            public void onProviderInstallFailed(int errorCode, Intent recoveryIntent) {
                GooglePlayServicesUtil.showErrorNotification(errorCode, MainApplication.this);
            }
        });
    }

    private void initApplicationComponent() {
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
