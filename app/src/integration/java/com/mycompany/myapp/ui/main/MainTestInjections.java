package com.mycompany.myapp.ui.main;

import com.mycompany.myapp.data.api.github.GitHubBusService;
import com.squareup.otto.Bus;

import javax.inject.Inject;

public class MainTestInjections {
    @Inject
    GitHubBusService gitHubBusService;

    @Inject
    protected Bus bus;
}
