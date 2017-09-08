package com.mycompany.myapp.monitoring

import timber.log.Timber

class LoggingOnlyCrashReporter : CrashReporter {
    override fun logMessage(message: String) = Timber.i(message)

    override fun logException(message: String, ex: Exception) = Timber.e(ex, message)
}
