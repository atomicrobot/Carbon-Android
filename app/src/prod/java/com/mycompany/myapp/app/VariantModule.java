package com.mycompany.myapp.app;

import android.support.annotation.Nullable;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

import dagger.Module;
import dagger.Provides;

@Module
public class VariantModule {

    @Nullable
    @Provides
    public HostnameVerifier provideCustomHostnameVerifier() {
        // No custom implementation needed in non-dev app
        return null;
    }

    @Nullable
    @Provides
    public SSLContext provideSSLContext() {
        // No custom implementation needed in non-dev app
        return null;
    }

    @Nullable
    @Provides
    public X509TrustManager buildTrustAllTrustManager() {
        // No custom implementation needed in non-dev app
        return null;
    }
}
