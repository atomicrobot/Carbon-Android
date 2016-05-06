package com.mycompany.myapp;

import android.app.Instrumentation;
import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.mycompany.myapp.app.MainApplication;

public class TestUtils {
    public static MainApplication getAppUnderTest() {
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        Context targetContext = instrumentation.getTargetContext();
        return (MainApplication) targetContext.getApplicationContext();
    }
}
