package com.atomicrobot.carbon.app

import android.app.Application
import com.atomicrobot.carbon.data.DataModule
import com.atomicrobot.carbon.modules.crashReporterModule
import com.atomicrobot.carbon.util.AppLogger
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
open class MainApplication : Application() {
    private lateinit var initializer: MainApplicationInitializer

    override fun onCreate() {
        super.onCreate()

        initializer = MainApplicationInitializer(this)

        initializeApplication()
    }

    /**
     * For Espresso tests we *do not* want to setupViewModel the full application and instead will
     * favor mocking out non-ui dependencies. This should be overridden to a no-op if initialization
     * should not occur.
     */
    protected open fun initializeApplication() {
        initializer.initialize()
    }

    open fun isTesting() = false
}
