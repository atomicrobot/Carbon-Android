package com.atomicrobot.carbon.ui.lumen.model

import com.atomicrobot.carbon.data.lumen.dto.LumenRoomAndAllScenesWithLights

data class RoomModel(
    private val source: LumenRoomAndAllScenesWithLights? = null,
    val roomId: Long = source?.room?.roomId ?: 0L,
    val roomName: String = source?.room?.name ?: "",
    val location: String = source?.room?.location ?: "",
) {
    val scenes: List<SceneModel> = source?.scenes?.map { scene ->
        SceneModel(
            scene.scene,
            scene.lights.map { LightModel(it, this) },
            this
        )
    }?.toMutableList() ?: emptyList()

    val lights: List<LightModel> =
        source?.scenes
            ?.map { it.lights }
            // Flatten the scenes list down into a list of lights and create models using
            // it
            ?.flatten()
            ?.map { LightModel(it, this) }
            ?.toMutableList() ?: emptyList()
}
