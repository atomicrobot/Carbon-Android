package com.atomicrobot.carbon.data.lumen

import com.atomicrobot.carbon.data.lumen.dto.LumenScene
import com.atomicrobot.carbon.data.lumen.dto.SceneAndLightsWithRoom

data class SceneModel(
    val sceneId: Long = 0,
    val name: String = "",
    val active: Boolean = false,
    val favorite: Boolean = false,
    val duration: String = "",
    val roomId: Long = 0,
    val roomName: String = "",
    val lights: List<Long> = emptyList()
) {
    constructor(scene: SceneAndLightsWithRoom) : this(
        sceneId = scene.sceneId,
        name = scene.sceneName,
        favorite = scene.favorite,
        duration = scene.duration,
        roomId = scene.roomId,
        roomName = scene.roomName,
        lights = scene.lights.map { it.lightId }
    )
}

fun SceneModel.toLumenScene(): LumenScene {
    return LumenScene(
        sceneId = this.sceneId,
        sceneName = this.name,
        containingRoomId = this.roomId,
        duration = this.duration,
        active = this.active,
        favorite = this.favorite
    )
}
