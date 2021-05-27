package com.atomicrobot.carbon.util

import android.content.SharedPreferences

fun SharedPreferences.putOrClearPreference(key: String, put: Boolean, value: Any) {
    if (put) {
        val editor = edit()
        when (value) {
            is Boolean -> editor.putBoolean(key, value)
            is String -> editor.putString(key, value)
        }
        editor.apply()
    } else {
        edit().remove(key).apply()
    }
}