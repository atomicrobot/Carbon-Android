package com.mycompany.myapp.ui.devsettings;

import com.mycompany.myapp.app.Settings;
import com.mycompany.myapp.ui.ActivityScope;
import com.mycompany.myapp.ui.devsettings.DevSettingsComponent.DevSettingsModule;

import dagger.Module;
import dagger.Provides;
import dagger.Subcomponent;

@ActivityScope
@Subcomponent(modules = DevSettingsModule.class)
public interface DevSettingsComponent {

    @Module
    class DevSettingsModule {
        private final DevSettingsActivity activity;

        DevSettingsModule(DevSettingsActivity activity) {
            this.activity = activity;
        }

        @ActivityScope
        @Provides
        protected DevSettingsPresenter providePresenter(Settings settings) {
            return new DevSettingsPresenter(settings);
        }
    }

    void inject(DevSettingsActivity activity);
    void inject(DevSettingsFragment fragment);
}
