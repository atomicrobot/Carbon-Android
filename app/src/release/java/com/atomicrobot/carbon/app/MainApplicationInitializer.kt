package com.atomicrobot.carbon.app

import android.app.Application
import com.atomicrobot.carbon.monitoring.model.NoOpTree
import javax.inject.Inject

/**
 * Specific to the production variant.
 */
class MainApplicationInitializer @Inject constructor(application: Application) : BaseApplicationInitializer(application, NoOpTree()) {

    override fun initialize() {
        super.initialize()
    }
}
