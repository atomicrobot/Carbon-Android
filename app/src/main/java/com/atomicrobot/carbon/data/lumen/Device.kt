package com.atomicrobot.carbon.data.lumen

import android.graphics.Color

data class Device(
    val name: String,
    val room: Room,
    val type: LightType = LightType.WHITE,
    val location: String = "Home",
    val color: Int = Color.WHITE,
    var brightness: Float = 1F
) {
    val active: Boolean = brightness > 0

    override fun toString(): String = name
}

enum class LightType {
    WHITE,
    COLOR
}
