package com.mycompany.myapp.monitoring

interface CrashReporter {
    fun logMessage(message: String)
    fun logException(message: String, ex: Exception)
}
