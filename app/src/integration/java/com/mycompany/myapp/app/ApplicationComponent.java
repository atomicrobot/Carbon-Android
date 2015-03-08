package com.mycompany.myapp.app;

import android.app.Application;

import com.mycompany.myapp.data.DataModule;
import com.mycompany.myapp.data.api.github.GitHubBusService;
import com.mycompany.myapp.data.api.github.GitHubModule;
import com.mycompany.myapp.data.api.github.MockGitHubModule;
import com.mycompany.myapp.modules.CrashReporterModule;
import com.mycompany.myapp.monitoring.CrashReporter;
import com.mycompany.myapp.monitoring.LoggerModule;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Component;
import retrofit.client.Client;
import timber.log.Timber.Tree;

@Singleton
@Component(modules = {
        AndroidModule.class,
        LoggerModule.class,
        CrashReporterModule.class,
        BusModule.class,
        MockGitHubModule.class
})
public interface ApplicationComponent {
    Application application();

    Tree tree();

    CrashReporter crashReporter();

    Bus bus();

    GitHubBusService gitHubBusService();

    void inject(MainApplication application);
}
