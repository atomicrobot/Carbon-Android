package com.mycompany.myapp.ui.devsettings;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.mycompany.myapp.R;
import com.mycompany.myapp.app.MainApplication;
import com.mycompany.myapp.ui.BaseActivity;
import com.mycompany.myapp.ui.devsettings.DevSettingsComponent.DevSettingsModule;
import com.mycompany.myapp.ui.devsettings.DevSettingsFragment.DevSettingsFragmentHost;
import com.mycompany.myapp.ui.devsettings.DevSettingsPresenter.DevSettingsViewContract;

import javax.inject.Inject;

public class DevSettingsActivity extends BaseActivity implements DevSettingsViewContract, DevSettingsFragmentHost {
    public static Intent buildIntent(Context context) {
        return new Intent(context, DevSettingsActivity.class);
    }

    private DevSettingsComponent component;

    @Inject DevSettingsPresenter presenter;

    private DevSettingsActivityBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        component = ((MainApplication) getApplication()).getComponent()
                .devSettingsComponent(new DevSettingsModule(this));
        component.inject(this);

        presenter.setView(this);
        lifecycleRegistry.addObserver(presenter);
        presenter.restoreState(savedInstanceState);

        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_dev_settings);
        binding.setPresenter(presenter);
        binding.executePendingBindings();

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("Dev Settings");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenter.saveState(outState);
    }

    @Override
    public void inject(DevSettingsFragment fragment) {
        component.inject(fragment);
    }
}
