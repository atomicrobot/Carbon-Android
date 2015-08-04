package com.mycompany.myapp.ui.main;

import com.mycompany.myapp.R;
import com.mycompany.myapp.ui.Resource;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {
    private final MainActivity activity;

    public MainModule(MainActivity activity) {
        this.activity = activity;
    }

    @Provides
    @Resource(R.string.author_format)
    java.lang.String authorFormat() {
        return activity.getString(R.string.author_format);
    }
}
