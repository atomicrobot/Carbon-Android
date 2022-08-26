package com.atomicrobot.carbon.data.lumen

data class Scene(
    val name: String,
    val room: Room,
    val devices: List<Device> = emptyList(),
    val duration: String,
    val active: Boolean = false,
    val favorite: Boolean = false,
) {
    override fun toString(): String = name
}
