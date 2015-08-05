package com.mycompany.myapp;

import android.support.annotation.NonNull;

public interface Stateful<State> {
    void save(@NonNull State state);
    @NonNull State restore(State defaultState);
}
