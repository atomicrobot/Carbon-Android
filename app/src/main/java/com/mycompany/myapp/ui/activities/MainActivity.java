package com.mycompany.myapp.ui.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.mycompany.myapp.MainApplication;
import com.mycompany.myapp.R;
import com.mycompany.myapp.monitoring.CrashReporter;

import javax.inject.Inject;

import hugo.weaving.DebugLog;

public class MainActivity extends ActionBarActivity {

    @Inject
    CrashReporter crashReporter;

    @DebugLog
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        ((MainApplication) getApplication()).getApplicationComponent().inject(this);

		setContentView(R.layout.activity_main);
	}
}
