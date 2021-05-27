package com.atomicrobot.carbon.app

import com.atomicrobot.carbon.data.DataModule
import com.atomicrobot.carbon.modules.CrashReporterModule
import com.atomicrobot.carbon.monitoring.LoggerModule
import com.atomicrobot.carbon.ui.ViewModelFactoryModule
import com.atomicrobot.carbon.StartActivity
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