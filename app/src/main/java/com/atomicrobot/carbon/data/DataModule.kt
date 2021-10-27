package com.atomicrobot.carbon.data

import android.content.Context
import com.atomicrobot.carbon.app.Settings
import com.atomicrobot.carbon.data.api.github.GitHubApiService
import com.atomicrobot.carbon.data.api.github.GitHubInteractor
import com.atomicrobot.carbon.ui.main.MainViewModel
import com.atomicrobot.carbon.ui.splash.SplashViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Cache
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File


interface OkHttpSecurityModifier {
    fun apply(builder: OkHttpClient.Builder)
}

class DataModule {
    val dataModule = module {
        single {
            val cacheDir = File(androidApplication().cacheDir, "http")
            Cache(cacheDir, DISK_CACHE_SIZE.toLong())
        }

        single {
            provideOkHttpClient(get(), get())
        }

        single(named(BASE_URL)) {
            provideBaseUrl(get())
        }

        single<Converter.Factory> {
            val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
            MoshiConverterFactory.create(moshi)
        }

        single {
            provideRetrofit(get(), get(qualifier = named(BASE_URL)), get())
        }

        single {
            provideGitHubApiService(get())
        }

        single {
            provideGitHubService(androidContext(), get())
        }

        viewModel {
            MainViewModel(
                androidApplication(),
                get(),
                get(qualifier = named("loading_delay_ms"))
            )
        }

        viewModel {
            SplashViewModel(androidApplication())
        }
    }

    private fun provideOkHttpClient(cache: Cache, securityModifier: OkHttpSecurityModifier): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.cache(cache)
        securityModifier.apply(builder)
        return builder.build()
    }

    private fun provideBaseUrl(settings: Settings): String {
        return settings.baseUrl
    }

    private fun provideRetrofit(
        client: OkHttpClient,
        baseUrl: String,
        converterFactory: Converter.Factory
    ): Retrofit {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(baseUrl)
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    private fun provideGitHubApiService(retrofit: Retrofit): GitHubApiService {
        return retrofit.create(GitHubApiService::class.java)
    }

    private fun provideGitHubService(
        context: Context,
        api: GitHubApiService
    ): GitHubInteractor {
        return GitHubInteractor(context, api)
    }

    companion object {
        private const val DISK_CACHE_SIZE = 50 * 1024 * 1024 // 50MB

        const val BASE_URL = "baseUrl"
    }
}
