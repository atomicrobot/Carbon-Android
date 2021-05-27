package com.atomicrobot.carbon

import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher

import java.util.regex.Pattern

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
