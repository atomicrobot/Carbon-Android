package com.mycompany.myapp.ui.devsettings;

import android.databinding.Bindable;

import com.mycompany.myapp.BR;
import com.mycompany.myapp.app.Settings;
import com.mycompany.myapp.ui.BasePresenter;
import com.mycompany.myapp.ui.devsettings.DevSettingsPresenter.DevSettingsViewContract;
import com.mycompany.myapp.ui.devsettings.DevSettingsPresenter.State;

import org.parceler.Parcel;

public class DevSettingsPresenter extends BasePresenter<DevSettingsViewContract, State> {
    private static final String STATE_KEY = "DevSettingsPresenterState";  // NON-NLS

    public interface DevSettingsViewContract {

    }

    @Parcel
    public static class State {
        boolean initialized = false;

        String baseUrl;
        boolean trustAllSSL;
    }

    private final Settings settings;

    public DevSettingsPresenter(Settings settings) {
        super(STATE_KEY, new State());
        this.settings = settings;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!state.initialized) {
            state.initialized = true;
            setBaseUrl(settings.getBaseUrl());
            setTrustAllSSL(settings.getTrustAllSSL());
        }
    }

    @Bindable
    public String getBaseUrl() {
        return state.baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        state.baseUrl = baseUrl;
        notifyPropertyChanged(BR.baseUrl);
    }

    @Bindable
    public boolean isTrustAllSSL() {
        return state.trustAllSSL;
    }

    public void setTrustAllSSL(boolean trustAllSSL) {
        state.trustAllSSL = trustAllSSL;
        notifyPropertyChanged(BR.trustAllSSL);
    }

    public void saveSettingsAndRestart() {
        settings.setBaseUrl(state.baseUrl);
        settings.setTrustAllSSL(state.trustAllSSL);
    }
}
