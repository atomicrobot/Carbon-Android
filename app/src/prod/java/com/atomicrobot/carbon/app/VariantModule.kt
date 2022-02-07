package com.atomicrobot.carbon.app

import com.atomicrobot.carbon.data.OkHttpSecurityModifier
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class VariantModule {
    @Singleton
    @Binds
    abstract fun bindOkHttpSecurityModifier(impl: NoOpSecurityModifier): OkHttpSecurityModifier
}

class NoOpSecurityModifier : OkHttpSecurityModifier {
    override fun apply(builder: OkHttpClient.Builder) {
        /* No op */
    }
}
