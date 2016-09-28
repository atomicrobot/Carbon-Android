package com.mycompany.myapp.ui.devsettings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mycompany.myapp.R;
import com.mycompany.myapp.app.MainApplication;
import com.mycompany.myapp.ui.devsettings.DevSettingsComponent.DevSettingsModule;
import com.mycompany.myapp.ui.devsettings.DevSettingsFragment.DevSettingsFragmentHost;
import com.mycompany.myapp.ui.devsettings.DevSettingsPresenter.DevSettingsViewContract;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DevSettingsActivity extends AppCompatActivity implements DevSettingsViewContract, DevSettingsFragmentHost {
    public static Intent buildIntent(Context context) {
        Intent intent = new Intent(context, DevSettingsActivity.class);
        return intent;
    }

    private DevSettingsComponent component;

    @Inject DevSettingsPresenter presenter;

    @BindView(R.id.root_layout) View rootLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;

    private DevSettingsFragment fragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        component = ((MainApplication) getApplication()).getComponent()
                .devSettingsComponent(new DevSettingsModule(this));
        component.inject(this);

        presenter.setView(this);
        presenter.restoreState(savedInstanceState);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_settings);
        ButterKnife.bind(this);
        fragment = (DevSettingsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_dev_settings);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("DevSettings");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenter.saveState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void onPause() {
        presenter.onPause();
        super.onPause();
    }

    @Override
    public void inject(DevSettingsFragment fragment) {
        component.inject(fragment);
    }

    @Override
    public void displayTrustAllSSL(boolean trustAllSSL) {
        fragment.displayTrustAllSSL(trustAllSSL);
    }

    @Override
    public void setTrustAllSSL(boolean trustAllSSL) {
        presenter.setTrustAllSSL(trustAllSSL);
    }

    @Override
    public void saveSettingsAndRestart() {
        presenter.saveSettingsAndRestart();
    }
}
