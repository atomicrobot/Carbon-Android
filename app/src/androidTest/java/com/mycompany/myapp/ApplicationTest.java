package com.mycompany.myapp;

import android.test.ApplicationTestCase;

public class ApplicationTest extends ApplicationTestCase<MainApplication> {
    public ApplicationTest() {
        super(MainApplication.class);
    }

    public void handleTestTrue() {
        assertNotNull(getApplication());
    }
}