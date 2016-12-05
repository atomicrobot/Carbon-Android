package com.mycompany.myapp.ui.devsettings;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jakewharton.processphoenix.ProcessPhoenix;
import com.mycompany.myapp.app.Settings;
import com.mycompany.myapp.util.RxUtils;

import org.parceler.Parcel;
import org.parceler.Parcels;

import rx.subscriptions.CompositeSubscription;

public class DevSettingsPresenter {
    private static final String EXTRA_STATE = "DevSettingsPresenterState";

    public interface DevSettingsViewContract {
        void displayBaseUrl(String baseUrl);

        void displayTrustAllSSL(boolean trustAllSSL);
    }

    @Parcel
    public static class State {
        String baseUrl;
        boolean trustAllSSL;
        boolean initialized = false;
    }

    private final Context context;
    private final Settings settings;

    private CompositeSubscription subscriptions;
    private DevSettingsViewContract view;
    private State state;

    public DevSettingsPresenter(Context context, Settings settings) {
        this.context = context;
        this.settings = settings;
        this.state = new State();
    }

    public void setView(DevSettingsViewContract view) {
        this.view = view;
    }

    public void saveState(@NonNull Bundle bundle) {
        bundle.putParcelable(EXTRA_STATE, Parcels.wrap(state));
    }

    public void restoreState(@Nullable Bundle bundle) {
        if (bundle != null && bundle.containsKey(EXTRA_STATE)) {
            state = Parcels.unwrap(bundle.getParcelable(EXTRA_STATE));
        }
    }

    public void onResume() {
        subscriptions = RxUtils.getNewCompositeSubIfUnsubscribed(subscriptions);
        if (!state.initialized) {
            // Do initial setup here
            state.initialized = true;
            state.baseUrl = settings.getBaseUrl();
            state.trustAllSSL = settings.getTrustAllSSL();
        }

        view.displayBaseUrl(state.baseUrl);
        view.displayTrustAllSSL(state.trustAllSSL);
    }

    public void onPause() {
        RxUtils.unsubscribeIfNotNull(subscriptions);
    }

    public void setBaseUrl(String baseUrl) {
        state.baseUrl = baseUrl;
    }

    public void setTrustAllSSL(boolean trustAllSSL) {
        state.trustAllSSL = trustAllSSL;
    }

    public void saveSettingsAndRestart() {
        settings.setBaseUrl(state.baseUrl);
        settings.setTrustAllSSL(state.trustAllSSL);

        ProcessPhoenix.triggerRebirth(context);
    }
}
