package com.mycompany.myapp.app

import com.mycompany.myapp.data.DataModule
import com.mycompany.myapp.modules.CrashReporterModule
import com.mycompany.myapp.monitoring.LoggerModule
import com.mycompany.myapp.ui.main.MainComponent
import com.mycompany.myapp.ui.main.MainComponent.MainModule
import dagger.Component
import javax.inject.Singleton

// GENERATOR - MORE IMPORTS //

@Singleton
@Component(modules = arrayOf(
        VariantModule::class,
        AndroidModule::class,
        AppModule::class,
        LoggerModule::class,
        CrashReporterModule::class,
        DataModule::class))
interface ApplicationComponent : VariantApplicationComponent {
    fun mainComponent(module: MainModule): MainComponent
    // GENERATOR - MORE SUBCOMPONENTS //

    fun inject(application: MainApplication)
}