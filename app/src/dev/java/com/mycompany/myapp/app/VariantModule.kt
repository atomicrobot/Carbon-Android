package com.mycompany.myapp.app

import dagger.Module
import dagger.Provides
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

@Module
class VariantModule {

    @Provides
    fun provideCustomHostnameVerifier(settings: Settings): HostnameVerifier? {
        return when {
            settings.trustAllSSL -> SSLDevelopmentHelper.buildTrustAllHostnameVerifier()
            else -> null
        }
    }

    @Provides
    fun provideSSLContext(settings: Settings): SSLContext? {
        return when {
            settings.trustAllSSL -> SSLDevelopmentHelper.buildTrustAllSSLContext()
            else -> null
        }
    }

    @Provides
    fun buildTrustAllTrustManager(settings: Settings): X509TrustManager? {
        return when {
            settings.trustAllSSL -> SSLDevelopmentHelper.buildTrustAllTrustManager()
            else -> null
        }
    }
}
