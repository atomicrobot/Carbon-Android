package com.atomicrobot.carbon.data.lumen.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = LumenRoom::class,
            parentColumns = arrayOf("roomId"),
            childColumns = arrayOf("containingRoomId")
        )
    ]
)
data class LumenScene(
    @PrimaryKey(autoGenerate = true) val sceneId: Long = 0L,
    val sceneName: String = "New Scene",
    @ColumnInfo(index = true)
    val containingRoomId: Long = 0L,
    val duration: String = "1 hour",
    val active: Boolean = false,
    val favorite: Boolean = false
) {
    override fun toString(): String = sceneName
}
