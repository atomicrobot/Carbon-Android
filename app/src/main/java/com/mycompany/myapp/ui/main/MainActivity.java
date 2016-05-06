package com.mycompany.myapp.ui.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mycompany.myapp.R;
import com.mycompany.myapp.app.MainApplication;
import com.mycompany.myapp.ui.main.MainComponent.MainModule;
import com.mycompany.myapp.ui.main.MainFragment.MainFragmentHost;

public class MainActivity extends AppCompatActivity implements MainFragmentHost {
    private MainComponent component;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        component = ((MainApplication) getApplication()).getComponent()
                .mainComponent(new MainModule(this));
        component.inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Home");
        getSupportActionBar().setSubtitle("Hi, person");
    }

    @Override
    public void inject(MainFragment fragment) {
        component.inject(fragment);
    }
}
