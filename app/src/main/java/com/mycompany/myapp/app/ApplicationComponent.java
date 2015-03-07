package com.mycompany.myapp.app;

import android.content.Context;

import com.mycompany.myapp.modules.CrashReporterModule;
import com.mycompany.myapp.monitoring.CrashReporter;
import com.squareup.otto.Bus;

import dagger.Component;

@Component(modules = {
        AndroidModule.class,
        CrashReporterModule.class,
        BusModule.class
})
public interface ApplicationComponent {
    @ForApplicationScope
    Context applicationContext();

    CrashReporter crashReporter();

    Bus bus();

    void inject(MainApplication application);
}
