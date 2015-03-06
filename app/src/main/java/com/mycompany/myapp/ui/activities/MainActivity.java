package com.mycompany.myapp.ui.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.mycompany.myapp.R;

import hugo.weaving.DebugLog;

public class MainActivity extends ActionBarActivity {

    @DebugLog
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
}
