package com.mycompany.myapp.data.api.github;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static org.mockito.Mockito.mock;

@Module
public class MockGitHubModule {
    @Singleton
    @Provides
    GitHubBusService provideGitHubBusService() {
        return mock(GitHubBusService.class);
    }
}
