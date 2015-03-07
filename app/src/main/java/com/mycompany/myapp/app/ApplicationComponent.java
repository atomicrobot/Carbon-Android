package com.mycompany.myapp.app;

import android.content.Context;

import com.mycompany.myapp.modules.CrashReporterModule;
import com.mycompany.myapp.modules.LoggerModule;
import com.mycompany.myapp.monitoring.CrashReporter;
import com.squareup.otto.Bus;

import dagger.Component;
import timber.log.Timber.Tree;

@Component(modules = {
        AndroidModule.class,
        LoggerModule.class,
        CrashReporterModule.class,
        BusModule.class
})
public interface ApplicationComponent {
    @ForApplicationScope
    Context applicationContext();

    @ForApplicationScope
    Tree tree();

    CrashReporter crashReporter();

    Bus bus();

    void inject(MainApplication application);
}
