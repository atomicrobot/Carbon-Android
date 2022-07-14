package com.atomicrobot.carbon.app

import com.atomicrobot.carbon.data.OkHttpSecurityModifier
import com.atomicrobot.carbon.ui.devsettings.DevSettingsViewModel
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
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
