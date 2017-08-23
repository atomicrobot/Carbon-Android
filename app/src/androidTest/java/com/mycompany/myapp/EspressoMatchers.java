package com.mycompany.myapp;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.util.regex.Pattern;

public class EspressoMatchers {
    public static Matcher<String> regex(final String regex) {
        return new BaseMatcher<String>() {
            @Override
            public boolean matches(Object item) {
                Pattern pattern = Pattern.compile(regex);
                return pattern.matcher(item.toString()).matches();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("regex '" + regex + "'");
            }
        };
    }
}
