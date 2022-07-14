package com.atomicrobot.carbon.data

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.atomicrobot.carbon.app.Settings
import com.atomicrobot.carbon.data.api.github.GitHubApiService
import com.atomicrobot.carbon.data.api.github.GitHubInteractor
import com.atomicrobot.carbon.deeplink.DeepLinkInteractor
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
            provideOkHttpClient(
                cache = get(),
                securityModifier = get()
            )
        }

        single(named(BASE_URL)) {
            provideBaseUrl(
                settings = get()
            )
        }

        single {
            val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
            MoshiConverterFactory.create(moshi) as Converter.Factory
        }

        single {
            provideRetrofit(
                client = get(),
                baseUrl = get(qualifier = named(BASE_URL)),
                converterFactory = get()
            )
        }

        single {
            provideGitHubApiService(
                retrofit = get()
            )
        }

        single {
            provideGitHubService(
                context = androidContext(),
                api = get()
            )
        }

        single {
            DeepLinkInteractor()
        }

        viewModel {
            SplashViewModel(
                deepLinkInteractor = get()
            )
        }

        viewModel {
            MainViewModel(
                app = androidApplication(),
                gitHubInteractor = get(),
                loadingDelayMs = get(qualifier = named("loading_delay_ms"))
            )
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

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun provideRetrofit(
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

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun provideGitHubApiService(retrofit: Retrofit): GitHubApiService {
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
