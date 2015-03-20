package com.mycompany.myapp.ui.main;

import com.mycompany.myapp.ui.main.MainFragment.MainFragmentListener;

import dagger.Module;
import dagger.Provides;

@Module
public class MainUIModule {
    private final MainActivity activity;

    public MainUIModule(MainActivity activity) {
        this.activity = activity;
    }

    @Provides
    MainFragmentListener provideMainFragmentListener() {
        return activity;
    }
}
