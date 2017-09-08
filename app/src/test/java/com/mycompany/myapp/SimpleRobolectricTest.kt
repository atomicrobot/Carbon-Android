package com.mycompany.myapp

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class SimpleRobolectricTest {

    @Test
    fun testAppName() {
        val application = RuntimeEnvironment.application
        val appName = application.getString(R.string.app_name)
        assertEquals("My App", appName)
    }
}
