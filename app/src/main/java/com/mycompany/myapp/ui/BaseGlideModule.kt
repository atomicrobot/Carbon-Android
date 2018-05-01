package com.mycompany.myapp.ui

import android.databinding.BindingAdapter
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions

object GlideBindingAdapter {
    @JvmStatic
    @BindingAdapter(
            value = [
                "url",
                "placeholder",
                "fallback",
                "error"],
            requireAll = false)
    fun setImageSource(
            view: ImageView,
            url: String?,
            placeholder: Drawable?,
            fallback: Drawable?,
            error: Drawable?) {
        GlideApp.with(view.context)
                .load(url)
                .apply(RequestOptions()
                        .fitCenter()
                        .placeholder(placeholder)
                        .fallback(fallback)
                        .error(error))
                .into(view)
    }
}

// Customize Glide configuration here...
// More info: https://bumptech.github.io/glide/doc/configuration.html#module-classes-and-annotations

@GlideModule
class BaseGlideModule : AppGlideModule() {
    override fun isManifestParsingEnabled() = false
}
