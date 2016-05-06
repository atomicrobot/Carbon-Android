package com.mycompany.myapp.ui.main;

import android.content.Context;

import com.mycompany.myapp.data.api.github.GitHubService;
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
        public MainPresenter providePresenter(Context context, GitHubService service) {
            return new MainPresenter(context, service);
        }
    }

    void inject(MainActivity activity);

    void inject(MainFragment fragment);
}
