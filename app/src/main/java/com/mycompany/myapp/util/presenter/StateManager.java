package com.mycompany.myapp.util.presenter;

import android.support.annotation.NonNull;

public interface StateManager<State> {
    void save(@NonNull State state);
    @NonNull State restore(State defaultState);
}
