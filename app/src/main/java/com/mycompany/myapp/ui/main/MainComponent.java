package com.mycompany.myapp.ui.main;

import com.mycompany.myapp.app.ApplicationComponent;
import com.mycompany.myapp.ui.ActivityModule;
import com.mycompany.myapp.ui.ForActivityScope;

import dagger.Component;

@Component(
        dependencies = ApplicationComponent.class,
        modules = ActivityModule.class
)
@ForActivityScope
public interface MainComponent {
    void inject(MainActivity activity);
    void inject(MainFragment fragment);
}
