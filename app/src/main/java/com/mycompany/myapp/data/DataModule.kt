package com.mycompany.myapp.data

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.mycompany.myapp.app.Settings
import com.mycompany.myapp.data.api.github.GitHubApiService
import com.mycompany.myapp.data.api.github.GitHubService
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import javax.inject.Singleton

@Module
class DataModule {

    @Singleton
    @Provides
    fun provideCache(app: Application): Cache {
        val cacheDir = File(app.cacheDir, "http")
        return Cache(cacheDir, DISK_CACHE_SIZE.toLong())
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(cache: Cache?): OkHttpClient {
        return OkHttpClient.Builder()
                .cache(cache)
                .build()
    }

    @Singleton
    @Provides
    fun provideBaseUrl(settings: Settings): String {
        return settings.baseUrl
    }

    @Singleton
    @Provides
    fun provideConverter(): Converter.Factory {
        val gson = Gson()
        return GsonConverterFactory.create(gson)
    }

    @Singleton
    @Provides
    fun provideRetrofit(
            client: OkHttpClient,
            baseUrl: String,
            converterFactory: Converter.Factory): Retrofit {
        return Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                .addConverterFactory(converterFactory)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    @Singleton
    @Provides
    fun provideGitHubApiService(retrofit: Retrofit): GitHubApiService {
        return retrofit.create(GitHubApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideGitHubService(
            context: Context,
            api: GitHubApiService): GitHubService {
        return GitHubService(context, api)
    }

    companion object {
        private const val DISK_CACHE_SIZE = 50 * 1024 * 1024 // 50MB
    }
}
