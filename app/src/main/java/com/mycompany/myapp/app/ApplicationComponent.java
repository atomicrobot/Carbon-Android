package com.mycompany.myapp.app;

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
    CrashReporter crashReporter();

    Bus bus();

    void inject(MainApplication application);
}
