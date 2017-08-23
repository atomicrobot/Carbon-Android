package com.mycompany.myapp.ui.devsettings

import com.mycompany.myapp.app.Settings
import com.mycompany.myapp.ui.ActivityScope
import com.mycompany.myapp.ui.devsettings.DevSettingsComponent.DevSettingsModule

import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = arrayOf(DevSettingsModule::class))
interface DevSettingsComponent {

    @Module
    class DevSettingsModule internal constructor(private val activity: DevSettingsActivity) {

        @ActivityScope
        @Provides
        fun providePresenter(settings: Settings): DevSettingsPresenter {
            return DevSettingsPresenter(settings)
        }
    }

    fun inject(activity: DevSettingsActivity)
    fun inject(fragment: DevSettingsFragment)
}
