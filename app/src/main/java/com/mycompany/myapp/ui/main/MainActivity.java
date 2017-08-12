package com.mycompany.myapp.ui.main;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;

import com.mycompany.myapp.MainActivityBinding;
import com.mycompany.myapp.R;
import com.mycompany.myapp.app.MainApplication;
import com.mycompany.myapp.ui.BaseActivity;
import com.mycompany.myapp.ui.main.MainComponent.MainModule;
import com.mycompany.myapp.ui.main.MainFragment.MainFragmentHost;
import com.mycompany.myapp.ui.main.MainPresenter.MainViewContract;

import javax.inject.Inject;

public class MainActivity extends BaseActivity implements MainViewContract, MainFragmentHost {
    private MainComponent component;

    @Inject MainPresenter presenter;

    private MainActivityBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        component = ((MainApplication) getApplication()).getComponent()
                .mainComponent(new MainModule(this));
        component.inject(this);

        presenter.setView(this);
        lifecycleRegistry.addObserver(presenter);
        presenter.restoreState(savedInstanceState);

        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setPresenter(presenter);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle(getApplicationContext().getPackageName());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenter.saveState(outState);
    }

    @Override
    public void inject(MainFragment fragment) {
        component.inject(fragment);
    }

    @Override
    public void displayError(String message) {
        Snackbar.make(binding.rootLayout, message, Snackbar.LENGTH_LONG).show();
    }
}