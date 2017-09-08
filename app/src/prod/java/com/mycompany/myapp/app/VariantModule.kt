package com.mycompany.myapp.app

import android.support.annotation.Nullable
import dagger.Module
import dagger.Provides
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

@Module
class VariantModule {

    // No custom implementation needed in non-dev app
    @Nullable
    @Provides
    fun provideCustomHostnameVerifier(): HostnameVerifier? = null

    // No custom implementation needed in non-dev app
    @Nullable
    @Provides
    fun provideSSLContext(): SSLContext? = null

    // No custom implementation needed in non-dev app
    @Nullable
    @Provides
    fun buildTrustAllTrustManager(): X509TrustManager? = null
}
