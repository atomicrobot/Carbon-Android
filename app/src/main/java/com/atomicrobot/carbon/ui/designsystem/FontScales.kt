package com.atomicrobot.carbon.ui.designsystem

import android.content.Context
import com.atomicrobot.carbon.R

val fontScales = listOf(
    FontScale.Small,
    FontScale.Normal,
    FontScale.Large,
    FontScale.ExtraLarge,
)

sealed class FontScale(val scale: Float) {
    object Small : FontScale(scale = .75F)
    object Normal : FontScale(scale = 1.0F)
    object Large : FontScale(scale = 1.5F)
    object ExtraLarge : FontScale(scale = 2.0F)

    fun label(ctx: Context): String {
        return ctx.getString(R.string.design_font_scale, this.scale)
    }
}