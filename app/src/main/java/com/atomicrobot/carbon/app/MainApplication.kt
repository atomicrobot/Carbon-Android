package com.atomicrobot.carbon.app

import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : CoreApplication() {

    override fun initializeApplication() {
        initializer = EntryPointAccessors.fromApplication(
            this, MainApplicationInitializerEntryPoint::class.java
        ).getMainApplicationInitializer()
        initializer.initialize()
    }

    override fun isTesting() = false
}
