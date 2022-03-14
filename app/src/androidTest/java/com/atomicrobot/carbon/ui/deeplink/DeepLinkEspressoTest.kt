package com.atomicrobot.carbon.ui.deeplink

import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.TextView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.hasTextColor
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.StartActivity
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DeepLinkEspressoTest {
    private lateinit var scenario: ActivityScenario<StartActivity>

    @Test
    fun testValidDeepLinkIntent() {
        val uri = "https://www.atomicrobot.com/carbon-android/path1"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))

        scenario = ActivityScenario.launch(intent)
        onView(withId(R.id.deep_link_text)).check(matches(isDisplayed()))
    }

    @Test
    fun testInvalidDeepLinkIntent() {
        // Pass in invalid deeplink path. Should then be navigated to main fragment screen
        val uri = "https://www.atomicrobot.com/carbon-android/path3"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))

        scenario = ActivityScenario.launch(intent)

        // Deep link text should not exist because uri was not valid
        onView(withId(R.id.deep_link_text)).check(doesNotExist())

        // Check to see if Main Fragment views are displaying
        onView(withId(R.id.appbar)).check(matches(isDisplayed()))
        onView(withId(R.id.header)).check(matches(isDisplayed()))
    }

    @Test
    fun testDeepLinkIntentWithTextColorValue() {
        val textColor = "blue"
        val uri = "https://www.atomicrobot.com/carbon-android/path1?textColor=$textColor"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))

        scenario = ActivityScenario.launch(intent)
        onView(withId(R.id.deep_link_text)).check(matches(hasTextColor(R.color.blue)))
    }

    @Test
    fun testDeepLinkIntentWithTextSizeValue() {
        val textSize = 22f
        val uri = "https://www.atomicrobot.com/carbon-android/path1?textSize=$textSize"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))

        scenario = ActivityScenario.launch(intent)

        onView(withId(R.id.deep_link_text)).check(matches(withFontSize(textSize)))
    }

    private fun withFontSize(expectedSize: Float): Matcher<View?> {
        return object : BoundedMatcher<View?, View>(View::class.java) {
            override fun matchesSafely(target: View): Boolean {
                if (target !is TextView) {
                    return false
                }
                val pixels = target.textSize
                val actualSize = pixels / target.getResources().displayMetrics.scaledDensity
                return actualSize.compareTo(expectedSize) == 0
            }

            override fun describeTo(description: Description) {
                description.appendText("with fontSize: ")
                description.appendValue(expectedSize)
            }
        }
    }
}