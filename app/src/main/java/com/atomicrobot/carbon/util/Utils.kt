package com.atomicrobot.carbon.util

import android.content.Intent
import androidx.activity.ComponentActivity

//region Activity extensions
inline fun <reified T: ComponentActivity> ComponentActivity.startComponentActivity() {
    startActivity(Intent(this, T::class.java))
}
//endregion

//region Helper functions
fun splitCamelCase(s: String): String {
    return s.replace(
        String.format(
            "%s|%s|%s",
            "(?<=[A-Z])(?=[A-Z][a-z])",
            "(?<=[^A-Z])(?=[A-Z])",
            "(?<=[A-Za-z])(?=[^A-Za-z])"
        ).toRegex(),
        "/"
    )
}
//endregion