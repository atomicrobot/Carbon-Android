package com.atomicrobot.carbon.util.databinding

import android.util.TypedValue
import android.widget.TextView
import androidx.databinding.BindingAdapter

object TextViewAdapters {
    @JvmStatic
    @BindingAdapter("textSize")
    fun setTextSize(textView: TextView, size: Float) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
    }
}