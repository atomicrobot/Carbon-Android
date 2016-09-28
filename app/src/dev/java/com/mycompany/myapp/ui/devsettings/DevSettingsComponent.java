package com.mycompany.myapp.ui.devsettings;

import android.content.Context;

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

        public DevSettingsModule(DevSettingsActivity activity) {
            this.activity = activity;
        }

        @ActivityScope
        @Provides
        public DevSettingsPresenter providePresenter(Context context, Settings settings) {
            return new DevSettingsPresenter(context, settings);
        }
    }

    void inject(DevSettingsActivity activity);
    void inject(DevSettingsFragment fragment);
}
