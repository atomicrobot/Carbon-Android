package com.mycompany.myapp.data.api.github;

import com.squareup.otto.Bus;

import dagger.Module;
import dagger.Provides;
import retrofit.Endpoint;
import retrofit.Endpoints;
import retrofit.RestAdapter;
import retrofit.client.Client;
import timber.log.Timber.Tree;

@Module
public class GitHubModule {
    @Provides
    Endpoint provideEndpoint() {
        return Endpoints.newFixedEndpoint("https://api.github.com");
    }

    @Provides
    RestAdapter provideRestAdapter(Endpoint endpoint, Client client) {
        return new RestAdapter.Builder()
                .setClient(client)
                .setEndpoint(endpoint)
                .build();
    }

    @Provides
    GitHubApiService provideGitHubService(RestAdapter restAdapter) {
        return restAdapter.create(GitHubApiService.class);
    }

    @Provides
    GitHubBusService provideGitHubBusService(GitHubApiService api, Bus bus, Tree logger) {
        return new GitHubBusService(api, bus, logger);
    }
}
