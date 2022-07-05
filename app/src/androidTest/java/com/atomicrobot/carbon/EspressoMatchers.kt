package com.atomicrobot.carbon

import android.view.View
import android.widget.TextView
import androidx.test.espresso.matcher.BoundedMatcher
import java.util.regex.Pattern
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher

object EspressoMatchers {
    fun regex(regex: String): Matcher<String> {
        return object : BaseMatcher<String>() {
            override fun matches(item: Any): Boolean {
                val pattern = Pattern.compile(regex)
                return pattern.matcher(item.toString()).matches()
            }

            override fun describeTo(description: Description) {
                description.appendText("regex '$regex'")
            }
        }
    }
}

fun withFontSize(expectedSize: Float): Matcher<View?> {
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