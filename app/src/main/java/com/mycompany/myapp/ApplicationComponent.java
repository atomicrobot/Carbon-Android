package com.mycompany.myapp;

import com.mycompany.myapp.modules.AndroidModule;
import com.mycompany.myapp.modules.CrashReporterModule;
import com.mycompany.myapp.ui.activities.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        AndroidModule.class,
        CrashReporterModule.class})
public interface ApplicationComponent {

    public final static class Initializer {
        public static ApplicationComponent init(MainApplication mainApplication) {
            return Dagger_ApplicationComponent.builder()
                    .androidModule(new AndroidModule(mainApplication))
                    .build();
        }
    }

    void inject(MainApplication application);
    void inject(MainActivity activity);
}
