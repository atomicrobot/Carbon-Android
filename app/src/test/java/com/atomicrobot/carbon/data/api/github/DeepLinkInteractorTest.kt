package com.atomicrobot.carbon.data.api.github

import android.graphics.Color
import android.net.Uri
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.deeplink.DeepLinkInteractor
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
class DeepLinkInteractorTest {

    private lateinit var interactor: DeepLinkInteractor

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        interactor = DeepLinkInteractor()
    }

    @Test
    fun testDeepLinkPath() {
        interactor.setDeepLinkUri(Uri.parse("https://www.atomicrobot.com/carbon-android/path1"))
        interactor.setDeepLinkPath("/carbon-android/path1")

        val navResource = interactor.getDeepLinkNavDestination()
        assertTrue(navResource == "deepLinkPath1")
    }

    @Test
    fun testDeepLinkPathTextColor() {
        interactor.setDeepLinkUri(Uri.parse("https://www.atomicrobot.com/carbon-android/path1?textColor=blue"))

        val resourceColor = interactor.getDeepLinkTextColor()
        assertTrue(resourceColor == Color.BLUE)
    }

    @Test
    fun testDeepLinkPathTextSize() {
        interactor.setDeepLinkUri(Uri.parse("https://www.atomicrobot.com/carbon-android/path1?textSize=22"))

        val resourceSize = interactor.getDeepLinkTextSize()
        assertTrue(resourceSize == 22f)
    }

    @Test
    fun testDeepLinkPathTextColorTextSize() {
        // Passing in bad data, function should use default values
        interactor.setDeepLinkUri(Uri.parse("https://www.atomicrobot.com/carbon-android/path1?textColor=razzle&textSize=22L"))
        interactor.setDeepLinkPath("/carbon-android/path1")

        val navResource = interactor.getDeepLinkNavDestination()
        assertTrue(navResource == "deepLinkPath1")

        val resourceColor = interactor.getDeepLinkTextColor()
        assertTrue(resourceColor == Color.BLACK)

        val resourceSize = interactor.getDeepLinkTextSize()
        assertTrue(resourceSize == 30f)
    }
}