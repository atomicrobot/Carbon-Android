package com.atomicrobot.carbon

import com.atomicrobot.carbon.app.AppModule
import com.atomicrobot.carbon.app.VariantModule
import com.atomicrobot.carbon.data.DataModule
import com.atomicrobot.carbon.modules.CrashReporterModule
import com.atomicrobot.carbon.modules.LoggerModule
import dagger.hilt.components.SingletonComponent
import it.cosenonjaviste.daggermock.DaggerMockRule

class MainApplicationDaggerMockRule : DaggerMockRule<SingletonComponent>(
        SingletonComponent::class.java,
        VariantModule::class,
        AppModule(0),
        LoggerModule,
        CrashReporterModule::class,
        DataModule) {
    init {
//        set { component -> getAppUnderTest().component = component }
    }
}
