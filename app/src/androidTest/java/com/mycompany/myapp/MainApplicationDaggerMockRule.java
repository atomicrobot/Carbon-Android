package com.mycompany.myapp;

import com.mycompany.myapp.app.AndroidModule;
import com.mycompany.myapp.app.AppModule;
import com.mycompany.myapp.app.ApplicationComponent;
import com.mycompany.myapp.app.VariantModule;
import com.mycompany.myapp.data.DataModule;
import com.mycompany.myapp.modules.CrashReporterModule;
import com.mycompany.myapp.monitoring.LoggerModule;

import it.cosenonjaviste.daggermock.DaggerMockRule;

import static com.mycompany.myapp.TestUtils.getAppUnderTest;

public class MainApplicationDaggerMockRule extends DaggerMockRule<ApplicationComponent> {
    public MainApplicationDaggerMockRule() {
        // If a module provides something that will be mocked out, it needs to be
        // included in this list of modules.
        // In other works, it is best to list out all of your app modules.
        super(ApplicationComponent.class,
                new VariantModule(),
                new AndroidModule(getAppUnderTest()),
                new AppModule(0),
                new LoggerModule(),
                new CrashReporterModule(),
                new DataModule());
        set(component -> getAppUnderTest().setComponent(component));
    }
}
