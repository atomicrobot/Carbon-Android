package com.atomicrobot.carbon.modules

import com.atomicrobot.carbon.monitoring.CrashReporter
import com.atomicrobot.carbon.monitoring.CrashlyticsCrashReporter
import org.koin.dsl.module

val crashReporterModule = module {
    single {
        CrashlyticsCrashReporter()
    }
}
