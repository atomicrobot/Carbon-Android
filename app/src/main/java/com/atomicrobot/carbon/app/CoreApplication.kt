package com.atomicrobot.carbon.app

import android.app.Application
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

open class CoreApplication : Application() {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface MainApplicationInitializerEntryPoint {
        fun getMainApplicationInitializer(): MainApplicationInitializer
    }

    lateinit var initializer: MainApplicationInitializer

    override fun onCreate() {
        super.onCreate()
        initializeApplication()
    }

    /**
     * For Espresso tests we *do not* want to setupViewModel the full application and instead will
     * favor mocking out non-ui dependencies. This should be overridden to a no-op if initialization
     * should not occur.
     */
    protected open fun initializeApplication() {

    }

    open fun isTesting() = true
}