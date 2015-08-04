package com.mycompany.myapp.app;

import com.mycompany.myapp.data.DataModule;
import com.mycompany.myapp.data.api.github.GitHubModule;
import com.mycompany.myapp.data.api.github.GitHubService;
import com.mycompany.myapp.modules.CrashReporterModule;
import com.mycompany.myapp.monitoring.LoggerModule;
import com.mycompany.myapp.ui.main.MainComponent;
import com.mycompany.myapp.ui.main.MainModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        AndroidModule.class,
        LoggerModule.class,
        CrashReporterModule.class,
        DataModule.class,
        GitHubModule.class
})
public interface ApplicationComponent {
    GitHubService gitHubService();

    MainComponent plus(MainModule module);

    void inject(MainApplication application);
}
