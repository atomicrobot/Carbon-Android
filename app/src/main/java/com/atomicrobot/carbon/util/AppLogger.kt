package com.atomicrobot.carbon.util

import org.koin.core.logger.Level
import org.koin.core.logger.Logger
import org.koin.core.logger.MESSAGE
import timber.log.Timber

class AppLogger : Logger() {
    override fun log(level: Level, msg: MESSAGE) {
        when (level) {
            Level.DEBUG -> Timber.d(msg)
            Level.ERROR -> Timber.e(msg)
            Level.INFO -> Timber.i(msg)
            Level.NONE -> {}
        }
    }
}