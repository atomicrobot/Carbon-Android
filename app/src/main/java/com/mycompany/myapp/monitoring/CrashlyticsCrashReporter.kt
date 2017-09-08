package com.mycompany.myapp.monitoring

import com.crashlytics.android.Crashlytics

class CrashlyticsCrashReporter : CrashReporter {
    override fun logMessage(message: String) = Crashlytics.log(message)

    override fun logException(message: String, ex: Exception) {
        Crashlytics.log(message)
        Crashlytics.logException(ex)
    }
}
