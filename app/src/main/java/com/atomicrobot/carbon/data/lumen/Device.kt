package com.atomicrobot.carbon.data.lumen

import android.graphics.Color

data class Device(
    val name: String,
    val room: Room,
    val location: String = "Home",
    val active: Boolean = false,
    val color: Int = Color.WHITE,
    val brightness: Int = 255
) {
    override fun toString(): String = name
}
