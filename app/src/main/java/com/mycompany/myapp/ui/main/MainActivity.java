package com.mycompany.myapp.ui.main;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mycompany.myapp.R;
import com.mycompany.myapp.app.MainApplication;
import com.mycompany.myapp.ui.main.MainComponent.MainModule;
import com.mycompany.myapp.ui.main.MainFragment.MainFragmentHost;
import com.mycompany.myapp.ui.main.MainPresenter.MainViewContract;
import com.mycompany.myapp.ui.main.MainPresenter.State;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainViewContract, MainFragmentHost {
    private MainComponent component;

    @Inject MainPresenter presenter;

    @BindView(R.id.root_layout) View rootLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;

    private MainFragment fragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        component = ((MainApplication) getApplication()).getComponent()
                .mainComponent(new MainModule(this));
        component.inject(this);

        presenter.setView(this);
        presenter.restoreState(savedInstanceState);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        fragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_main);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");
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
    public void inject(MainFragment fragment) {
        component.inject(fragment);
    }

    @Override
    public void setUsername(String username) {
        presenter.setUsername(username);
    }

    @Override
    public void setRepository(String repository) {
        presenter.setRepository(repository);
    }

    @Override
    public void fetchCommits() {
        presenter.fetchCommits();
    }

    @Override
    public void render(State state) {
        fragment.render(state);
    }

    @Override
    public void displayError(String message) {
        Snackbar.make(rootLayout, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void displayVersion(String version) {
        fragment.displayVersion(version);
    }

    @Override
    public void displayFingerprint(String fingerprint) {
        fragment.displayFingerprint(fingerprint);
    }
}
