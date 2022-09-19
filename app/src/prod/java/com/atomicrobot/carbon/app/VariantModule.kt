package com.atomicrobot.carbon.app

import okhttp3.OkHttpClient
import org.koin.dsl.module

val variantModule = module {
    single {
        NoOpSecurityModifier() as OkHttpSecurityModifier
    }
}

class NoOpSecurityModifier : OkHttpSecurityModifier {
    override fun apply(builder: OkHttpClient.Builder) {
        /* No op */
    }
}
