package com.mycompany.myapp.util.databinding

import android.view.View
import androidx.databinding.BindingAdapter

object ViewAdapters {
    @JvmStatic
    @BindingAdapter("visible")
    fun setVisible(view: View, visible: Boolean) {
        view.visibility = if (visible) View.VISIBLE else View.INVISIBLE
    }
}
