package com.atomicrobot.carbon.data.lumen.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LumenRoom(
    @PrimaryKey val roomId: Long,
    val name: String,
    val location: String = "Home"
) {
    override fun toString(): String = name
}
