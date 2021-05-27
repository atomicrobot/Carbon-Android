package com.atomicrobot.carbon.app

import android.app.Application
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

/**
 * Specific to the production variant.
 */
class MainApplicationInitializer(
        application: Application,
        logger: Timber.Tree) : BaseApplicationInitializer(application, logger) {

    override fun initialize() {
        super.initialize()
    }
}
