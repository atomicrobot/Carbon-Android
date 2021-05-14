package com.atomicrobot.carbon

import android.os.Bundle
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito.mock

class SimpleTests {
    @Test
    fun testTrueIsTrue() {
        assertTrue(true)
    }

    @Test
    fun testMocking() {
        val mockBundle = mock(Bundle::class.java)
        whenever(mockBundle.getString("key")).thenReturn("value")

        val value = mockBundle.getString("key")
        assertEquals("value", value)
    }
}
