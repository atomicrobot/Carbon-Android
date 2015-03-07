package com.mycompany.myapp.ui.main;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.mycompany.myapp.R;
import com.mycompany.myapp.app.MainApplication;
import com.mycompany.myapp.monitoring.CrashReporter;
import com.mycompany.myapp.ui.ActivityModule;
import com.squareup.otto.Bus;

import javax.inject.Inject;

import hugo.weaving.DebugLog;

public class MainActivity extends ActionBarActivity {

    private MainComponent component;

    @Inject
    CrashReporter crashReporter;

    @Inject
    Bus bus;

    @DebugLog
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        component = Dagger_MainComponent.builder()
                .applicationComponent(((MainApplication) getApplication()).getApplicationComponent())
                .activityModule(new ActivityModule(this))
                .build();
        component.inject(this);

        crashReporter.logMessage("Hello!");

        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bus.register(this);
    }

    @Override
    protected void onPause() {
        bus.unregister(this);
        super.onPause();
    }
}
