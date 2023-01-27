package com.atomicrobot.carbon.ui.deeplink

import android.graphics.Color
import android.net.Uri
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.atomicrobot.carbon.StartActivity
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
        MockitoAnnotations.openMocks(this)
        interactor = DeepLinkInteractor()
    }

    @Test
    fun testDeepLinkPath() {
        interactor.setDeepLinkUri(Uri.parse("https://www.atomicrobot.com/carbon-android/path1"))
        interactor.setDeepLinkPath("/carbon-android/path1")

        val navResource = interactor.getDeepLinkNavDestination()
        assertTrue(navResource == StartActivity.deepLinkPath1)
    }

    @Test
    fun testDeepLinkPathTextColor() {
        interactor.setDeepLinkUri(
            Uri.parse("https://www.atomicrobot.com/carbon-android/path1?textColor=blue")
        )

        val resourceColor = interactor.getDeepLinkTextColor()
        assertTrue(resourceColor == Color.BLUE)
    }

    @Test
    fun testDeepLinkPathTextSize() {
        interactor.setDeepLinkUri(
            Uri.parse("https://www.atomicrobot.com/carbon-android/path1?textSize=22")
        )

        val resourceSize = interactor.getDeepLinkTextSize()
        assertTrue(resourceSize == 22f)
    }

    @Test
    fun testDeepLinkPathTextColorTextSize() {
        // Passing in bad data, function should use default values
        interactor.setDeepLinkUri(
            Uri.parse(
                "https://www.atomicrobot.com/carbon-android/path1?textColor=razzle&textSize=22L"
            )
        )
        interactor.setDeepLinkPath("/carbon-android/path1")

        val navResource = interactor.getDeepLinkNavDestination()
        assertTrue(navResource == StartActivity.deepLinkPath1)

        val resourceColor = interactor.getDeepLinkTextColor()
        assertTrue(resourceColor == Color.BLACK)

        val resourceSize = interactor.getDeepLinkTextSize()
        assertTrue(resourceSize == 30f)
    }
}