package com.mycompany.myapp.ui.main;

import com.mycompany.myapp.data.DataModule;
import com.mycompany.myapp.ui.ActivityScope;
import com.mycompany.myapp.ui.main.SampleComponent.SampleModule;

import dagger.Module;
import dagger.Subcomponent;

@ActivityScope
@Subcomponent(modules = {SampleModule.class,
        DataModule.class})
public interface SampleComponent {

    @Module
    class SampleModule {
        private final SampleActivity activity;

        public SampleModule(SampleActivity activity) {
            this.activity = activity;
        }
    }

    void inject(SampleActivity activity);

    void inject(SampleFragment fragment);
}
