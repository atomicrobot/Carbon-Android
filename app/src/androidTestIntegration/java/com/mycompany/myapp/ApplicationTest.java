package com.mycompany.myapp;

import android.test.ApplicationTestCase;

import com.mycompany.myapp.app.MainApplication;

// TODO - Rules project should be including an application test rule soon...
public class ApplicationTest extends ApplicationTestCase<MainApplication> {
    public ApplicationTest() {
        super(MainApplication.class);
    }

    public void handleTestTrue() {
        assertNotNull(getApplication());
    }
}