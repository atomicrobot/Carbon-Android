package com.atomicrobot.carbon.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.atomicrobot.carbon.ui.main.MainViewModel
import com.atomicrobot.carbon.ui.splash.SplashViewModel
// GENERATOR - MORE IMPORTS //
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelFactoryModule: VariantViewModelFactoryModule() {
    @Binds internal abstract fun bindViewModelFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindsMainViewModel(mainViewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun bindsSplashViewModel(splashViewModel: SplashViewModel): ViewModel

    // GENERATOR - MORE VIEW MODELS //
}