package com.mycompany.myapp.app

import com.mycompany.myapp.data.DataModule
import com.mycompany.myapp.modules.CrashReporterModule
import com.mycompany.myapp.monitoring.LoggerModule
import com.mycompany.myapp.ui.ViewModelFactoryModule
import com.mycompany.myapp.StartActivity
// GENERATOR - MORE IMPORTS //
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
        VariantModule::class,
        AndroidModule::class,
        AppModule::class,
        LoggerModule::class,
        CrashReporterModule::class,
        DataModule::class,
        ViewModelFactoryModule::class])
interface ApplicationComponent : VariantApplicationComponent {
    fun inject(application: MainApplication)

    fun inject(activity: StartActivity)
    // GENERATOR - MORE ACTIVITIES //
}