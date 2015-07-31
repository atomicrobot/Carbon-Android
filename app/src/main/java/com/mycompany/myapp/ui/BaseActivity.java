package com.mycompany.myapp.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mycompany.myapp.app.ApplicationComponent;
import com.mycompany.myapp.app.HasComponent;
import com.mycompany.myapp.app.MainApplication;
import com.mycompany.myapp.monitoring.CrashReporter;

import javax.inject.Inject;

import hugo.weaving.DebugLog;
import timber.log.Timber.Tree;

public abstract class BaseActivity<T> extends AppCompatActivity implements HasComponent<T> {
    @Inject
    protected Tree logger;

    @Inject
    protected CrashReporter crashReporter;

    private T component;

    @DebugLog
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        component = buildComponent();
    }

    protected abstract T buildComponent();

    @Override
    public T getComponent() {
        return component;
    }

    protected ApplicationComponent getApplicationComponent() {
        return ((MainApplication) getApplication()).getComponent();
    }
}
