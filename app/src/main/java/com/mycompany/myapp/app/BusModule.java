package com.mycompany.myapp.app;

import com.squareup.otto.Bus;

import dagger.Module;
import dagger.Provides;
import hugo.weaving.DebugLog;

@Module
public class BusModule {
    private final Bus bus = new Bus();

    @DebugLog
    @Provides
    Bus provideBus() {
        return bus;
    }
}
