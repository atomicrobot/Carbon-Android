package com.atomicrobot.carbon.ui.lumen.model

import android.graphics.Color
import com.atomicrobot.carbon.data.lumen.dto.LightType
import com.atomicrobot.carbon.data.lumen.dto.LumenLight

data class LightModel(
    val sourceLight: LumenLight? = null,
    val containingRoom: RoomModel? = null,
    var lightId: Long = sourceLight?.lightId ?: 0L,
    var name: String = sourceLight?.name ?: "",
    var type: LightType = sourceLight?.type ?: LightType.WHITE,
    var color: Int = sourceLight?.color ?: Color.WHITE,
    var brightness: Float = sourceLight?.brightness ?: 1.0F
) {

    val containingRoomId: Long
        get() = containingRoom?.roomId ?: 0L

    val active: Boolean
        get() = brightness > 0F

    val colorTemperature: String = "Warm White"
}
