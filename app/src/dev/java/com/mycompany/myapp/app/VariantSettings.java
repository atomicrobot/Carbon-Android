package com.mycompany.myapp.app;

import android.content.Context;
import android.content.SharedPreferences;

public class VariantSettings {
    private static final String PREFS_SETTINGS = "settings";

    private static final String PREF_TRUST_ALL_SSL = "trust_all_ssl";

    private final Context context;

    public VariantSettings(Context context) {
        this.context = context;
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
