package com.atomicrobot.carbon.app

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AndroidModule(private val application: MainApplication) {
    @Singleton
    @Provides
    fun provideContext(): Context  = application

    @Singleton
    @Provides
    fun provideApplication(): Application = application
}
