package com.atomicrobot.carbon.data.lumen.dto

import androidx.room.Embedded
import androidx.room.Relation

data class LumenRoomAndAllScenes(
    @Embedded val room: LumenRoom,
    @Relation(
        entity = LumenScene::class,
        parentColumn = "roomId",
        entityColumn = "containingRoomId"
    )
    val scenes: List<LumenScene>
)

data class LumenRoomAndAllScenesWithLights(
    @Embedded val room: LumenRoom,
    @Relation(
        entity = LumenScene::class,
        parentColumn = "roomId",
        entityColumn = "containingRoomId"
    )
    val scenes: List<SceneWithLights>
)
