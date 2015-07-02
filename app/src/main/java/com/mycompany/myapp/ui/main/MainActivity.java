package com.mycompany.myapp.ui.main;

import android.os.Bundle;

import com.mycompany.myapp.R;
import com.mycompany.myapp.ui.BaseActivity;
import com.mycompany.myapp.ui.main.MainFragment.MainFragmentListener;

import hugo.weaving.DebugLog;

public class MainActivity extends BaseActivity<MainComponent> implements MainFragmentListener {
    @Override
    protected MainComponent buildComponent() {
        return getApplicationComponent()
                .plus(new MainUIModule(this));
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
