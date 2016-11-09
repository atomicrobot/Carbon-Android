package com.mycompany.myapp.app;

import com.mycompany.myapp.data.DataModule;
import com.mycompany.myapp.modules.CrashReporterModule;
import com.mycompany.myapp.monitoring.LoggerModule;
import com.mycompany.myapp.ui.main.MainComponent;
import com.mycompany.myapp.ui.main.MainComponent.MainModule;

import javax.inject.Singleton;

import dagger.Component;

// GENERATOR - MORE IMPORTS //

@Singleton
@Component(modules = {
        VariantModule.class,
        AndroidModule.class,
        AppModule.class,
        LoggerModule.class,
        CrashReporterModule.class,
        DataModule.class
})
public interface ApplicationComponent extends VariantApplicationComponent {
    MainComponent mainComponent(MainModule module);
    // GENERATOR - MORE SUBCOMPONENTS //

    void inject(MainApplication application);
}
