package com.mycompany.myapp.data.api.github;

import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.Endpoint;
import retrofit.Endpoints;
import retrofit.RestAdapter;
import retrofit.client.Client;
import timber.log.Timber.Tree;

@Module
public class GitHubModule {
    @Singleton
    @Api("github")
    @Provides
    Endpoint provideEndpoint() {
        return Endpoints.newFixedEndpoint("https://api.github.com");
    }

    @Singleton
    @Api("github")
    @Provides
    RestAdapter provideRestAdapter(@Api("github") Endpoint endpoint, Client client) {
        return new RestAdapter.Builder()
                .setClient(client)
                .setEndpoint(endpoint)
                .build();
    }

    @Singleton
    @Provides
    GitHubApiService provideGitHubService(@Api("github") RestAdapter restAdapter) {
        return restAdapter.create(GitHubApiService.class);
    }

    @Singleton
    @Provides
    GitHubBusService provideGitHubBusService(GitHubApiService api, Bus bus, Tree logger) {
        return new GitHubBusService(api, bus, logger);
    }
}
