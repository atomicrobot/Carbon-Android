package com.atomicrobot.carbon.app

import com.atomicrobot.carbon.data.OkHttpSecurityModifier
import okhttp3.OkHttpClient
import org.koin.dsl.module

val variantModule = module {
    single<OkHttpSecurityModifier> {
        NoOpSecurityModifier()
    }
}

class NoOpSecurityModifier : OkHttpSecurityModifier {
    override fun apply(builder: OkHttpClient.Builder) {
        /* No op */
    }
}
