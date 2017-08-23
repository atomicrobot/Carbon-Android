package com.mycompany.myapp.ui.main;

import android.content.Context;

import com.mycompany.myapp.data.api.github.GitHubService;
import com.mycompany.myapp.ui.ActivityScope;
import com.mycompany.myapp.ui.main.MainComponent.MainModule;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import dagger.Subcomponent;
import io.reactivex.Scheduler;

@ActivityScope
@Subcomponent(modules = MainModule.class)
public interface MainComponent {

    @Module
    class MainModule {
        private final MainActivity activity;

        MainModule(MainActivity activity) {
            this.activity = activity;
        }

        @ActivityScope
        @Provides
        protected MainPresenter providePresenter(
                Context context,
                GitHubService service,
                @Named("io") Scheduler ioScheduler,
                @Named("main") Scheduler mainScheduler,
                @Named("loading_delay_ms") long loadingDelayMs) {
            return new MainPresenter(context, service, ioScheduler, mainScheduler, loadingDelayMs);
        }
    }

    void inject(MainActivity activity);
    void inject(MainFragment fragment);
}
