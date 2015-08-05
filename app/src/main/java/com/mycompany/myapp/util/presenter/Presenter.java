package com.mycompany.myapp.util.presenter;

import android.support.annotation.NonNull;

public abstract class Presenter<ViewContract, SavedState> {
    @NonNull protected SavedState savedState;

    protected ViewContract view;

    public Presenter(@NonNull SavedState savedState) {
        this.savedState = savedState;
    }

    public void setView(ViewContract view) {
        this.view = view;
    }

    public void saveState(StateManager<SavedState> stateManager) {
        stateManager.save(savedState);
    }

    public void restoreState(StateManager<SavedState> stateManager) {
        savedState = stateManager.restore(savedState);
    }

    public abstract void onResume();

    public abstract void onPause();
}
