package com.atomicrobot.carbon.ui.lumen.model

import com.atomicrobot.carbon.data.lumen.dto.LumenScene

data class SceneModel(
    val scene: LumenScene? = null,
    val lights: List<LightModel> = listOf(),
    val containingRoom: RoomModel? = null,
    val sceneId: Long = scene?.sceneId ?: 0,
    val name: String = scene?.name ?: "",
    val duration: String = scene?.duration ?: "",
    val active: Boolean = scene?.active ?: false,
    val favorite: Boolean = scene?.favorite ?: false,
) {
    val containingRoomId: Long
        get() = containingRoom?.roomId ?: 0L

    val owningRoomName: String
        get() = containingRoom?.roomName ?: ""
}
