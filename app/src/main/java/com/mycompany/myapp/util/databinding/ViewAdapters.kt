package com.mycompany.myapp.util.databinding

import androidx.databinding.BindingAdapter
import android.view.View

object ViewAdapters {
    @JvmStatic
    @BindingAdapter("visible")
    fun setVisible(view: View, visible: Boolean) {
        view.visibility = if (visible) View.VISIBLE else View.INVISIBLE
    }
}
