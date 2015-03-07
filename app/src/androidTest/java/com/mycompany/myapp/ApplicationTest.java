package com.mycompany.myapp;

import android.test.ApplicationTestCase;

import com.mycompany.myapp.app.MainApplication;

public class ApplicationTest extends ApplicationTestCase<MainApplication> {
    public ApplicationTest() {
        super(MainApplication.class);
    }

    public void handleTestTrue() {
        assertNotNull(getApplication());
    }
}