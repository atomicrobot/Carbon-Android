package com.mycompany.myapp

import android.support.test.InstrumentationRegistry
import com.mycompany.myapp.app.MainApplication

object TestUtils {
    fun getAppUnderTest(): MainApplication {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val targetContext = instrumentation.targetContext
        return targetContext.applicationContext as MainApplication
    }
}
