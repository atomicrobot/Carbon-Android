package com.mycompany.myapp.app

import android.content.Context
import android.text.TextUtils
import com.mycompany.myapp.R
import com.mycompany.myapp.util.putOrClearPreference

open class VariantSettings(private val context: Context) {

    private val preferences = context.getSharedPreferences(PREFS_SETTINGS, Context.MODE_PRIVATE)

    var baseUrl: String
        get() = preferences.getString(PREF_BASE_URL, context.getString(R.string.default_base_url))
        set(baseUrl) = preferences.putOrClearPreference(PREF_BASE_URL, !TextUtils.isEmpty(baseUrl), baseUrl)

    var trustAllSSL: Boolean
        get() = preferences.getBoolean(PREF_TRUST_ALL_SSL, false)
        set(trustAllSSL) = preferences.putOrClearPreference(PREF_TRUST_ALL_SSL, trustAllSSL, trustAllSSL)

    companion object {
        private const val PREFS_SETTINGS = "settings"  // NON-NLS

        private const val PREF_BASE_URL = "base_url"  // NON-NLS
        private const val PREF_TRUST_ALL_SSL = "trust_all_ssl"  // NON-NLS
    }
}
