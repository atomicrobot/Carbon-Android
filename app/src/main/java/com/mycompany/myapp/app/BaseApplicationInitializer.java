package com.mycompany.myapp.app;

import android.app.Application;
import android.content.Intent;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.security.ProviderInstaller;
import com.google.android.gms.security.ProviderInstaller.ProviderInstallListener;
import com.squareup.leakcanary.LeakCanary;

import timber.log.Timber;
import timber.log.Timber.Tree;

public abstract class BaseApplicationInitializer {
    protected final Application application;
    private final Tree logger;

    public BaseApplicationInitializer(
            Application application,
            Tree logger) {
        this.application = application;
        this.logger = logger;
    }

    public void initialize() {
        LeakCanary.install(application);

        Timber.plant(logger);

        upgradeSecurityProvider();
    }

    private void upgradeSecurityProvider() {
        ProviderInstaller.installIfNeededAsync(application, new ProviderInstallListener() {
            @Override
            public void onProviderInstalled() {

            }

            @Override
            public void onProviderInstallFailed(int errorCode, Intent recoveryIntent) {
                GoogleApiAvailability.getInstance().showErrorNotification(application, errorCode);
            }
        });
    }
}
