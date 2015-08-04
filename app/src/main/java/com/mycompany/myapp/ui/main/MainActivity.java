package com.mycompany.myapp.ui.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mycompany.myapp.R;
import com.mycompany.myapp.app.MainApplication;
import com.mycompany.myapp.ui.main.MainFragment.MainFragmentHost;

public class MainActivity extends AppCompatActivity implements MainFragmentHost {
    private MainComponent component;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        component = ((MainApplication) getApplication()).getComponent()
                .plus(new MainModule(this));
        component.inject(this);

        setContentView(R.layout.activity_main);
    }

    @Override
    public void inject(MainFragment fragment) {
        component.inject(fragment);
    }
}
