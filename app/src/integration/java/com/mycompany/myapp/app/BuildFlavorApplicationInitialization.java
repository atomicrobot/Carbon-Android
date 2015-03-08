package com.mycompany.myapp.app;

public class BuildFlavorApplicationInitialization {
    private final MainApplication application;

    public BuildFlavorApplicationInitialization(MainApplication application) {
        this.application = application;
    }

    public void immediateInitialization() {
        System.setProperty("dexmaker.dexcache", application.getCacheDir().getPath());
    }
}
