package com.mycompany.myapp

import com.mycompany.myapp.TestUtils.getAppUnderTest
import com.mycompany.myapp.app.AndroidModule
import com.mycompany.myapp.app.AppModule
import com.mycompany.myapp.app.ApplicationComponent
import com.mycompany.myapp.app.VariantModule
import com.mycompany.myapp.data.DataModule
import com.mycompany.myapp.modules.CrashReporterModule
import com.mycompany.myapp.monitoring.LoggerModule
import it.cosenonjaviste.daggermock.DaggerMockRule

class MainApplicationDaggerMockRule : DaggerMockRule<ApplicationComponent>(
        ApplicationComponent::class.java,
        VariantModule(),
        AndroidModule(getAppUnderTest()),
        AppModule(0),
        LoggerModule(),
        CrashReporterModule(),
        DataModule()) {
    init {
        set { component -> getAppUnderTest().component = component }
    }
}
