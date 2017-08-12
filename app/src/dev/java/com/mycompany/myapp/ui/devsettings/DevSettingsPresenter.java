package com.mycompany.myapp.ui.devsettings;

import android.databinding.BaseObservable;
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
    public static class State extends BaseObservable {
        boolean initialized = false;

        String baseUrl;
        boolean trustAllSSL;

        @Bindable
        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            notifyPropertyChanged(BR.baseUrl);
        }

        @Bindable
        public boolean isTrustAllSSL() {
            return trustAllSSL;
        }

        public void setTrustAllSSL(boolean trustAllSSL) {
            this.trustAllSSL = trustAllSSL;
            notifyPropertyChanged(BR.trustAllSSL);
        }
    }

    private final Settings settings;

    public DevSettingsPresenter(Settings settings) {
        super(STATE_KEY);
        this.settings = settings;

        this.state = new State();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!state.initialized) {
            state.initialized = true;
            state.setBaseUrl(settings.getBaseUrl());
            state.setTrustAllSSL(settings.getTrustAllSSL());
        }
    }

    public void saveSettingsAndRestart() {
        settings.setBaseUrl(state.getBaseUrl());
        settings.setTrustAllSSL(state.isTrustAllSSL());
    }
}
