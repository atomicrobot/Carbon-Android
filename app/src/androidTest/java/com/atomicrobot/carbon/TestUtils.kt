package com.atomicrobot.carbon

import androidx.test.platform.app.InstrumentationRegistry
import com.atomicrobot.carbon.app.MainApplication

object TestUtils {
    fun getAppUnderTest(): MainApplication {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val targetContext = instrumentation.targetContext
        return targetContext.applicationContext as MainApplication
    }
}
