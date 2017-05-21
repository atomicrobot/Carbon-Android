package com.mycompany.myapp.app;

import android.content.Context;
import android.content.SharedPreferences;

import com.mycompany.myapp.R;

public class VariantSettings {
    private static final String PREFS_SETTINGS = "settings";

    private static final String PREF_BASE_URL = "base_url";
    private static final String PREF_TRUST_ALL_SSL = "trust_all_ssl";

    private final Context context;

    public VariantSettings(Context context) {
        this.context = context;
    }

    public String getBaseUrl() {
        String defaultBaseUrl = context.getString(R.string.default_base_url);
        return getSettingsSharedPreferences().getString(PREF_BASE_URL, defaultBaseUrl);
    }

    public void setBaseUrl(String baseUrl) {
        getSettingsSharedPreferences().edit().putString(PREF_BASE_URL, baseUrl).apply();
    }

    public boolean getTrustAllSSL() {
        return getSettingsSharedPreferences().getBoolean(PREF_TRUST_ALL_SSL, false);
    }

    public void setTrustAllSSL(boolean trustAllSSL) {
        getSettingsSharedPreferences().edit().putBoolean(PREF_TRUST_ALL_SSL, trustAllSSL).apply();
    }

    private SharedPreferences getSettingsSharedPreferences() {
        return context.getSharedPreferences(PREFS_SETTINGS, Context.MODE_PRIVATE);
    }
}
