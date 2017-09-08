package com.mycompany.myapp.util.databinding

import android.databinding.BindingAdapter
import android.support.v4.widget.ContentLoadingProgressBar
import android.view.View

object ViewAdapters {
    @JvmStatic
    @BindingAdapter("visible")
    fun setVisible(view: View, visible: Boolean) {
        view.visibility = if (visible) View.VISIBLE else View.INVISIBLE
    }

    @JvmStatic
    @BindingAdapter("showing_progress")
    fun setVisible(view: ContentLoadingProgressBar, visible: Boolean) {
        if (visible) {
            view.show()
        } else {
            view.hide()
        }
    }
}
