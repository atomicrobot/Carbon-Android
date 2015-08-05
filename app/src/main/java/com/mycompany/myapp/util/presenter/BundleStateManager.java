package com.mycompany.myapp.util.presenter;

import android.os.Bundle;
import android.support.annotation.NonNull;

import org.parceler.Parcels;

public class BundleStateManager<State> implements StateManager<State> {
    private static final String EXTRA_STATE = "state";

    private final Bundle bundle;

    public BundleStateManager(Bundle bundle) {
        this.bundle = bundle;
    }

    @Override
    public void save(@NonNull State state) {
        bundle.putParcelable(EXTRA_STATE, Parcels.wrap(state));
    }

    @Override
    @NonNull
    public State restore(@NonNull State defaultState) {
        if (bundle == null || !bundle.containsKey(EXTRA_STATE)) {
            return defaultState;
        }

        return Parcels.unwrap(bundle.getParcelable(EXTRA_STATE));
    }
}
