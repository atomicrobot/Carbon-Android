package com.mycompany.myapp.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.mycompany.myapp.R;

public class VariantSettings {
    private static final String PREFS_SETTINGS = "settings";  // NON-NLS

    private static final String PREF_BASE_URL = "base_url";  // NON-NLS
    private static final String PREF_TRUST_ALL_SSL = "trust_all_ssl";  // NON-NLS

    private final Context context;

    public VariantSettings(Context context) {
        this.context = context;
    }

    public String getBaseUrl() {
        String defaultBaseUrl = context.getString(R.string.default_base_url);
        return getSettingsSharedPreferences().getString(PREF_BASE_URL, defaultBaseUrl);
    }

    public void setBaseUrl(String baseUrl) {
        if (TextUtils.isEmpty(baseUrl)) {
            getSettingsSharedPreferences().edit().remove(PREF_BASE_URL).apply();
        } else {
            getSettingsSharedPreferences().edit().putString(PREF_BASE_URL, baseUrl).apply();
        }
    }

    public boolean getTrustAllSSL() {
        return getSettingsSharedPreferences().getBoolean(PREF_TRUST_ALL_SSL, false);
    }

    public void setTrustAllSSL(boolean trustAllSSL) {
        if (trustAllSSL) {
            getSettingsSharedPreferences().edit().putBoolean(PREF_TRUST_ALL_SSL, trustAllSSL).apply();
        } else {
            getSettingsSharedPreferences().edit().remove(PREF_TRUST_ALL_SSL).apply();
        }
    }

    private SharedPreferences getSettingsSharedPreferences() {
        return context.getSharedPreferences(PREFS_SETTINGS, Context.MODE_PRIVATE);
    }
}
