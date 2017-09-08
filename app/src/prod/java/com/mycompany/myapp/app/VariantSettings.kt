package com.mycompany.myapp.app

import android.content.Context
import com.mycompany.myapp.R

open class VariantSettings(private val context: Context) {
    val baseUrl: String = context.getString(R.string.default_base_url)
}
