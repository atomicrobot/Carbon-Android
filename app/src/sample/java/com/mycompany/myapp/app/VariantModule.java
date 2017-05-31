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
    public HostnameVerifier provideCustomHostnameVerifier(Settings settings) {
        if (settings.getTrustAllSSL()) {
            return SSLDevelopmentHelper.buildTrustAllHostnameVerifer();
        } else {
            return null;
        }
    }

    @Nullable
    @Provides
    public SSLContext provideSSLContext(Settings settings) {
        if (settings.getTrustAllSSL()) {
            return SSLDevelopmentHelper.buildTrustAllSSLContext();
        } else {
            return null;
        }
    }

    @Nullable
    @Provides
    public X509TrustManager buildTrustAllTrustManager(Settings settings) {
        if (settings.getTrustAllSSL()) {
            return SSLDevelopmentHelper.buildTrustAllTrustManager();
        } else {
            return null;
        }
    }
}
