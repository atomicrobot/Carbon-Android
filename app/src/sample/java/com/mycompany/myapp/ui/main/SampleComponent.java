package com.mycompany.myapp.ui.main;

import android.content.Context;

import com.mycompany.myapp.data.DataModule;
import com.mycompany.myapp.data.api.github.GitHubService;
import com.mycompany.myapp.ui.ActivityScope;
import com.mycompany.myapp.ui.main.SampleComponent.SampleModule;

import dagger.Module;
import dagger.Provides;
import dagger.Subcomponent;

@ActivityScope
@Subcomponent(modules = {SampleModule.class,
        DataModule.class})
public interface SampleComponent {

    @Module
    class SampleModule {
        private final SampleActivity activity;

        public SampleModule(SampleActivity activity) {
            this.activity = activity;
        }

        @ActivityScope
        @Provides
        public SamplePresenter providePresenter(Context context, GitHubService service) {
            return new SamplePresenter(context, service);
        }
    }

    void inject(SampleActivity activity);

    void inject(SampleFragment fragment);
}
