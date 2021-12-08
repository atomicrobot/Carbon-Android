package com.atomicrobot.carbon.app

import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

class AppModule(private val loadingDelayMs: Long = LOADING_DELAY_MS) {

    val appModule = module {
        single {
            Settings(context = androidContext())
        }

        single(named("loading_delay_ms")) {
            loadingDelayMs
        }
    }

    companion object {
        private const val LOADING_DELAY_MS: Long = 500
    }
}
