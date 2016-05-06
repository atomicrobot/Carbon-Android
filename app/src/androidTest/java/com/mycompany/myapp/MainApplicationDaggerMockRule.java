package com.mycompany.myapp;

import com.mycompany.myapp.app.AndroidModule;
import com.mycompany.myapp.app.ApplicationComponent;

import it.cosenonjaviste.daggermock.DaggerMockRule;

import static com.mycompany.myapp.TestUtils.getAppUnderTest;

public class MainApplicationDaggerMockRule extends DaggerMockRule<ApplicationComponent> {
    public MainApplicationDaggerMockRule() {
        super(ApplicationComponent.class,
                new AndroidModule(getAppUnderTest()));
        set(component -> getAppUnderTest().setComponent(component));
    }
}
