package com.atomicrobot.carbon.monitoring

import com.google.firebase.crashlytics.FirebaseCrashlytics

class CrashlyticsCrashReporter : CrashReporter {

    override fun logMessage(message: String) = FirebaseCrashlytics.getInstance().log(message)

    override fun logException(message: String, ex: Exception) {
        FirebaseCrashlytics.getInstance().log(message)
        FirebaseCrashlytics.getInstance().recordException(ex)
    }
}
