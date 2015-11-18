package com.mycompany.myapp.data.api.github;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.data.api.Api;
import com.squareup.okhttp.OkHttpClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.Converter;
import retrofit.JacksonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
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
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return JacksonConverterFactory.create(objectMapper);
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
