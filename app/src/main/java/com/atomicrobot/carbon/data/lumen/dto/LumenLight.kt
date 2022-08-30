package com.atomicrobot.carbon.data.lumen.dto

import android.graphics.Color
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
data class LumenLight(
    @PrimaryKey val lightId: Long,
    val name: String,
    @ColumnInfo(index = true)
    val containingRoomId: Long,
    val type: LightType = LightType.WHITE,
    val color: Int = Color.WHITE,
    val brightness: Float = 1F
) {
    override fun toString(): String = name
}

enum class LightType {
    WHITE,
    COLOR
}
