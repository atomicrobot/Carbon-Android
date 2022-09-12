package com.atomicrobot.carbon.modules

import com.atomicrobot.carbon.monitoring.LoggingOnlyCrashReporter
import org.koin.dsl.module

val crashReporterModule = module {
    single {
        LoggingOnlyCrashReporter()
    }
}
