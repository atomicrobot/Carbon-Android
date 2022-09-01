package com.atomicrobot.carbon.data.lumen.dto

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Junction
import androidx.room.Relation

@Entity(
    primaryKeys = ["sceneId", "lightId"],
    foreignKeys = arrayOf(
        ForeignKey(
            entity = LumenScene::class,
            parentColumns = arrayOf("sceneId"),
            childColumns = arrayOf("sceneId")
        ),
        ForeignKey(
            entity = LumenLight::class,
            parentColumns = arrayOf("lightId"),
            childColumns = arrayOf("lightId")
        )
    )
)

data class LumenSceneLightCrossRef(
    @ColumnInfo(index = true)
    val sceneId: Long,
    @ColumnInfo(index = true)
    val lightId: Long,
)

data class SceneWithLights(
    @Embedded val scene: LumenScene,
    @Relation(
        parentColumn = "sceneId",
        entityColumn = "lightId",
        associateBy = Junction(LumenSceneLightCrossRef::class)
    )
    val lights: List<LumenLight>
)

data class RoomNameAndId(
    val roomId: Long = 0L,
    val roomName: String = ""
)

data class SceneAndRoomName(
    @Embedded
    val scene: LumenScene,
    @Relation(
        parentColumn = "containingRoomId",
        entityColumn = "roomId",
        entity = LumenRoom::class,
    )
    val room: RoomNameAndId
)

data class SceneAndLightsWithRoom(
    @Embedded
    val scene: LumenScene,
    @Relation(
        parentColumn = "sceneId",
        entityColumn = "lightId",
        associateBy = Junction(LumenSceneLightCrossRef::class)
    )
    val lights: List<LumenLight>,
    @Relation(
        parentColumn = "containingRoomId",
        entityColumn = "roomId",
        entity = LumenRoom::class,
    )
    val room: RoomNameAndId
)