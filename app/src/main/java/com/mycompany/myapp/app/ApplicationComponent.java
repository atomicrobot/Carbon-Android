package com.mycompany.myapp.app;

import android.content.Context;

import com.mycompany.myapp.data.DataModule;
import com.mycompany.myapp.data.api.github.GitHubBusService;
import com.mycompany.myapp.data.api.github.GitHubModule;
import com.mycompany.myapp.modules.CrashReporterModule;
import com.mycompany.myapp.monitoring.CrashReporter;
import com.mycompany.myapp.monitoring.LoggerModule;
import com.squareup.otto.Bus;

import dagger.Component;
import retrofit.RestAdapter;
import retrofit.client.Client;
import timber.log.Timber.Tree;

@Component(modules = {
        AndroidModule.class,
        LoggerModule.class,
        CrashReporterModule.class,
        BusModule.class,
        DataModule.class,
        GitHubModule.class
})
public interface ApplicationComponent {
    @ForApplicationScope
    Context applicationContext();

    @ForApplicationScope
    Tree tree();

    CrashReporter crashReporter();

    Bus bus();

    Client client();

    RestAdapter restAdapter();

    GitHubBusService gitHubBusService();

    void inject(MainApplication application);
}
