package com.atomicrobot.carbon.app

import com.atomicrobot.carbon.data.OkHttpSecurityModifier
import dagger.Binds
import okhttp3.OkHttpClient
import javax.inject.Inject
import javax.inject.Singleton

abstract class VariantModule {
    @Singleton
    @Binds
    abstract fun bindDevSecurityModifier(impl: DevSecurityModifier): OkHttpSecurityModifier
}

class DevSecurityModifier @Inject constructor(val settings: Settings) : OkHttpSecurityModifier {
    override fun apply(builder: OkHttpClient.Builder) {
        if (settings.trustAllSSL) {
            SSLDevelopmentHelper.applyTrustAllSettings(builder)
        }
    }
}
