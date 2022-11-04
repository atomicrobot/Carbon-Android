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
    @PrimaryKey(autoGenerate = true) val lightId: Long = 0L,
    val lightName: String,
    @ColumnInfo(index = true)
    val containingRoomId: Long = 0L,
    val type: LightType = LightType.WHITE,
    val color: Int = Color.WHITE,
    val brightness: Float = 1F
) {
    val active: Boolean
        get() = brightness > 0.0F

    val colorTemperature: String
        get() = "Warm White"

    override fun toString(): String = lightName
}

enum class LightType {
    WHITE,
    WHITE_SMALL,
    COLOR,
    COLOR_SMALL,
    COLOR_STRIP,
}
