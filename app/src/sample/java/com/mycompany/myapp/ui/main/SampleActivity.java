package com.mycompany.myapp.ui.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mycompany.myapp.R;
import com.mycompany.myapp.app.MainApplication;
import com.mycompany.myapp.ui.main.SampleComponent.SampleModule;
import com.mycompany.myapp.ui.main.SampleFragment.SampleFragmentHost;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SampleActivity extends AppCompatActivity implements SampleFragmentHost {
    private SampleComponent component;

    @BindView(R.id.root_layout) View rootLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;

    private SampleFragment fragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        component = ((MainApplication) getApplication()).getComponent()
                .sampleComponent(new SampleModule(this));
        component.inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        ButterKnife.bind(this);
        fragment = (SampleFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_sample);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getApplicationContext().getPackageName());
    }

    @Override
    public void inject(SampleFragment fragment) {
        component.inject(fragment);
    }

}
