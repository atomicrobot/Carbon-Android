package com.mycompany.myapp.ui.main;

import dagger.Subcomponent;

@Subcomponent(modules = MainModule.class)
public interface MainComponent {
    MainPresenter mainPresenter();

    void inject(MainActivity activity);

    void inject(MainFragment fragment);
}
