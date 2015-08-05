package com.mycompany.myapp;

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

    public void saveState(Stateful<SavedState> stateful) {
        stateful.save(savedState);
    }

    public void restoreState(Stateful<SavedState> stateful) {
        savedState = stateful.restore(savedState);
    }

    public abstract void onResume();

    public abstract void onPause();
}
