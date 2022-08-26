package com.atomicrobot.carbon.data.lumen

data class Room(
    val name: String,
    val location: String = "Home",
) {
    val devices: MutableList<Device> = mutableListOf()

    val scenes: MutableList<Scene> = mutableListOf()

    override fun toString(): String = name
}
