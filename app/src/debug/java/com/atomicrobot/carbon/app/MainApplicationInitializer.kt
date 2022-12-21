package com.atomicrobot.carbon.app

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import com.atomicrobot.carbon.ui.RiseAndShine
import timber.log.Timber.Tree
import javax.inject.Inject

/**
 * Specific to the debug variant.
 */
class MainApplicationInitializer @Inject constructor(
    application: Application,
    logger: Tree
) :
    BaseApplicationInitializer(application, logger) {

    override fun initialize() {
        super.initialize()
        application.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                RiseAndShine.riseAndShine(activity)
            }

            override fun onActivityStarted(activity: Activity) {
            }

            override fun onActivityResumed(activity: Activity) {
            }

            override fun onActivityPaused(activity: Activity) {
            }

            override fun onActivityStopped(activity: Activity) {
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            }

            override fun onActivityDestroyed(activity: Activity) {
            }
        })
    }
}
