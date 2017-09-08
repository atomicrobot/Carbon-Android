package com.mycompany.myapp.app

import android.app.Application
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import timber.log.Timber

/**
 * Specific to the production variant.
 */
class MainApplicationInitializer(
        application: Application,
        logger: Timber.Tree) : BaseApplicationInitializer(application, logger) {

    override fun initialize() {
        super.initialize()
        Fabric.with(application, Crashlytics())
    }
}
