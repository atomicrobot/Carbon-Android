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
    @PrimaryKey val sceneId: Long,
    val name: String,
    @ColumnInfo(index = true)
    val containingRoomId: Long,
    val duration: String,
    val active: Boolean = false,
    val favorite: Boolean = false
) {
    override fun toString(): String = name
}
