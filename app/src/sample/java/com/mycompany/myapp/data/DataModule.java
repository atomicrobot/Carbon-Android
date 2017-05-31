package com.mycompany.myapp.data;

import android.app.Application;

import com.google.gson.Gson;
import com.mycompany.myapp.app.Settings;
import com.mycompany.myapp.data.api.Api;
import com.mycompany.myapp.data.api.github.GitHubApiService;
import com.mycompany.myapp.data.api.github.GitHubService;

import java.io.File;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class DataModule {
    private static final int DISK_CACHE_SIZE = 50 * 1024 * 1024; // 50MB

    @Singleton
    @Provides
    public Cache provideCache(Application app) {
        File cacheDir = new File(app.getCacheDir(), "http");
        return new Cache(cacheDir, DISK_CACHE_SIZE);
    }

    @Singleton
    @Provides
    public OkHttpClient provideOkHttpClient(Cache cache) {
        return new OkHttpClient.Builder()
                .cache(cache)
                .build();
    }

    @Singleton
    @Api("github")
    @Provides
    public String provideBaseUrl(Settings settings) {
        return settings.getBaseUrl();
    }

    @Singleton
    @Api("github")
    @Provides
    public Converter.Factory provideConverter() {
        Gson gson = new Gson();
        return GsonConverterFactory.create(gson);
    }

    @Singleton
    @Api("github")
    @Provides
    public Retrofit provideRetrofit(
            OkHttpClient client,
            @Api("github") String baseUrl,
            @Api("github") Converter.Factory converterFactory) {
        return new Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                .addConverterFactory(converterFactory)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    public GitHubApiService provideGitHubApiService(@Api("github") Retrofit retrofit) {
        return retrofit.create(GitHubApiService.class);
    }

    @Singleton
    @Provides
    public GitHubService provideGitHubService(GitHubApiService api) {
        return new GitHubService(api);
    }
}
