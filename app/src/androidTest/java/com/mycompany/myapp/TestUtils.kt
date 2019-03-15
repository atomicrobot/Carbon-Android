package com.mycompany.myapp

import androidx.test.platform.app.InstrumentationRegistry
import com.mycompany.myapp.app.MainApplication

object TestUtils {
    fun getAppUnderTest(): MainApplication {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val targetContext = instrumentation.targetContext
        return targetContext.applicationContext as MainApplication
    }
}
