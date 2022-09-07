package com.atomicrobot.carbon.data.lumen.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LumenRoom(
    @PrimaryKey(autoGenerate = true) val roomId: Long,
    val roomName: String,
    val location: String = "Home"
) {
    override fun toString(): String = roomName
}
