package com.mycompany.myapp.ui.main;

import android.os.Bundle;

import com.mycompany.myapp.R;
import com.mycompany.myapp.ui.ActivityModule;
import com.mycompany.myapp.ui.BaseActivity;

import hugo.weaving.DebugLog;

public class MainActivity extends BaseActivity<MainComponent> {
    @Override
    protected MainComponent buildComponent() {
        return Dagger_MainComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(new ActivityModule(this))
                .build();
    }

    @DebugLog
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent().inject(this);

        setContentView(R.layout.activity_main);

        crashReporter.logMessage("woohoo from the activity!");

        logger.i("Activity is ready to go!");
    }
}
