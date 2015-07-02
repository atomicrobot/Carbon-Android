package com.mycompany.myapp.ui.main;

import dagger.Subcomponent;

@Subcomponent(modules = MainUIModule.class)
public interface MainComponent {
    void inject(MainActivity activity);
    void inject(MainFragment fragment);
}
