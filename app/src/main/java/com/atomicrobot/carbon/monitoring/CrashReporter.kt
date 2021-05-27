package com.atomicrobot.carbon.monitoring

interface CrashReporter {
    fun logMessage(message: String)
    fun logException(message: String, ex: Exception)
}
