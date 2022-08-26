package com.atomicrobot.carbon.data.lumen

import android.graphics.Color

data class Scene(
    val name: String,
    val room: Room,
    val devices: MutableList<SceneDevice> = mutableListOf(),
    val duration: String,
    val active: Boolean = false,
    val favorite: Boolean = false,
) {
    override fun toString(): String = name
}

data class SceneDevice(
    val scene: Scene,
    val device: Device,
    val color: Int = Color.WHITE,
    val brightness: Float = 1F
) {
    val active: Boolean
        get() = brightness > 0

    val brightnessString: String
        get() = "${ (100 * brightness) }%"

    val colorTemperature: String = "Warm White"
}
