package com.atomicrobot.carbon.data

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.atomicrobot.carbon.app.Settings
import com.atomicrobot.carbon.data.api.github.GitHubApiService
import com.atomicrobot.carbon.data.api.github.GitHubInteractor
import com.atomicrobot.carbon.ui.deeplink.DeepLinkInteractor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
annotation class BaseUrl

interface OkHttpSecurityModifier {
    fun apply(builder: OkHttpClient.Builder)
}

@InstallIn(SingletonComponent::class)
@Module
object DataModule {
    @Provides
    fun provideCache(@ApplicationContext appContext: Context): Cache {
        val cacheDir = File(appContext.cacheDir, "http")
        return Cache(cacheDir, DISK_CACHE_SIZE.toLong())
    }

    @Provides
    fun provideOkHttpClient(cache: Cache, securityModifier: OkHttpSecurityModifier): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.cache(cache)
        securityModifier.apply(builder)
        return builder.build()
    }

    @Provides
    fun provideConverterFactory(): Converter.Factory {
        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        return MoshiConverterFactory.create(moshi) as Converter.Factory
    }

    @BaseUrl
    @Provides
    fun provideBaseUrl(settings: Settings): String {
        return settings.baseUrl
    }

    @Provides
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun provideRetrofit(
        client: OkHttpClient,
        @BaseUrl baseUrl: String,
        converterFactory: Converter.Factory
    ): Retrofit {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(baseUrl)
            .addConverterFactory(converterFactory)
            .build()
    }

    @Provides
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun provideGitHubApiService(retrofit: Retrofit): GitHubApiService {
        return retrofit.create(GitHubApiService::class.java)
    }

    @Provides
    fun provideGitHubService(
        @ApplicationContext context: Context,
        api: GitHubApiService
    ): GitHubInteractor {
        return GitHubInteractor(context, api)
    }

    @Singleton
    @Provides
    fun provideDeepLinkInteractor(): DeepLinkInteractor {
        return DeepLinkInteractor()
    }

    private const val DISK_CACHE_SIZE = 50 * 1024 * 1024 // 50MB
}
