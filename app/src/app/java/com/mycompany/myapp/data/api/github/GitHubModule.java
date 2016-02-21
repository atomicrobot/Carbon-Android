package com.mycompany.myapp.data.api.github;

import com.google.gson.Gson;
import com.mycompany.myapp.data.api.Api;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber.Tree;

@Module
public class GitHubModule {
    @Singleton
    @Api("github")
    @Provides
    String provideBaseUrl() {
        return "https://api.github.com";  // NON-NLS
    }

    @Singleton
    @Api("github")
    @Provides
    Converter.Factory provideConverter() {
        Gson gson = new Gson();
        return GsonConverterFactory.create(gson);
    }

    @Singleton
    @Api("github")
    @Provides
    Retrofit provideRetrofit(
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
    GitHubApiService provideGitHubService(@Api("github") Retrofit retrofit) {
        return retrofit.create(GitHubApiService.class);
    }

    @Singleton
    @Provides
    GitHubService provideGitHubBusService(GitHubApiService api, Tree logger) {
        return new GitHubService(api, logger);
    }
}
