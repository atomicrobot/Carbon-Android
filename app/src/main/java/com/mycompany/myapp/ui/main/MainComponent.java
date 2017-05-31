package com.mycompany.myapp.ui.main;

import com.mycompany.myapp.ui.ActivityScope;
import com.mycompany.myapp.ui.main.MainComponent.MainModule;

import dagger.Module;
import dagger.Provides;
import dagger.Subcomponent;

@ActivityScope
@Subcomponent(modules = MainModule.class)
public interface MainComponent {

    @Module
    class MainModule {
        private final MainActivity activity;

        public MainModule(MainActivity activity) {
            this.activity = activity;
        }

        @ActivityScope
        @Provides
        public MainPresenter providePresenter() {
            return new MainPresenter();
        }
    }

    void inject(MainActivity activity);

    void inject(MainFragment fragment);
}
