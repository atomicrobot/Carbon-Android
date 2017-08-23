package com.mycompany.myapp.ui.main

import android.content.Context

import com.mycompany.myapp.data.api.github.GitHubService
import com.mycompany.myapp.ui.ActivityScope
import com.mycompany.myapp.ui.main.MainComponent.MainModule

import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import io.reactivex.Scheduler
import javax.inject.Named

@ActivityScope
@Subcomponent(modules = arrayOf(MainModule::class))
interface MainComponent {

    @Module
    class MainModule internal constructor(private val activity: MainActivity) {

        @ActivityScope
        @Provides
        fun providePresenter(
                context: Context, service:
                GitHubService,
                @Named("io") ioScheduler: Scheduler,
                @Named("main") mainScheduler: Scheduler): MainPresenter {
            return MainPresenter(
                    context,
                    service,
                    ioScheduler,
                    mainScheduler)
        }
    }

    fun inject(activity: MainActivity)
    fun inject(fragment: MainFragment)
}
