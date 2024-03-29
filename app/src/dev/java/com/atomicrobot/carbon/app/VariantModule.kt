package com.atomicrobot.carbon.app

import okhttp3.OkHttpClient
import org.koin.dsl.module

val variantModule = module {
    single {
        DevSecurityModifier(
            settings = get()
        ) as OkHttpSecurityModifier // Cast is needed - compiler lies
    }

//    viewModel {
//        DevSettingsViewModel(
//            app = androidApplication(),
//            settings = get()
//        )
//    }
}

class DevSecurityModifier(val settings: Settings) : OkHttpSecurityModifier {
    override fun apply(builder: OkHttpClient.Builder) {
        if (settings.trustAllSSL) {
            SSLDevelopmentHelper.applyTrustAllSettings(builder)
        }
    }
}
