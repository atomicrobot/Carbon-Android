package com.atomicrobot.carbon.app

import android.content.Context
import com.atomicrobot.carbon.R

open class VariantSettings(private val context: Context) {
    val baseUrl: String = context.getString(R.string.default_base_url)
}
