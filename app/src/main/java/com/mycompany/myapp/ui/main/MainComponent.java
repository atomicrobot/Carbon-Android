package com.mycompany.myapp.ui.main;

import com.mycompany.myapp.data.api.github.GitHubService;

import dagger.Subcomponent;

@Subcomponent(modules = MainUIModule.class)
public interface MainComponent {
    GitHubService gitHubService();

    void inject(MainActivity activity);
    void inject(MainFragment fragment);
}
