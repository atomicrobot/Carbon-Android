package com.atomicrobot.carbon.ui

import androidx.lifecycle.ViewModel
import com.atomicrobot.carbon.ui.devsettings.DevSettingsViewModel
import com.atomicrobot.carbon.ui.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class VariantViewModelFactoryModule {
    @Binds
    @IntoMap
    @ViewModelKey(DevSettingsViewModel::class)
    abstract fun bindsDevSettingsViewModel(devSettingsViewModel: DevSettingsViewModel): ViewModel
}