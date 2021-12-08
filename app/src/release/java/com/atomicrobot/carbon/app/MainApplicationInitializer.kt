package com.atomicrobot.carbon.app

import android.app.Application
import com.atomicrobot.carbon.monitoring.model.NoOpTree

/**
 * Specific to the production variant.
 */
class MainApplicationInitializer(application: Application) : BaseApplicationInitializer(application, NoOpTree()) {

    override fun initialize() {
        super.initialize()
    }
}
