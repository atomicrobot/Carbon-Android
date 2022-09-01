package com.atomicrobot.carbon.data.lumen.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import com.atomicrobot.carbon.data.lumen.dto.LumenRoom

@Dao
interface RoomDao {
    @Update
    suspend fun update(room: LumenRoom): Int

    @Update
    suspend fun update(room: List<LumenRoom>): Int

    @Insert
    suspend fun insert(room: LumenRoom): Long

    @Insert
    suspend fun insert(room: List<LumenRoom>): List<Long>

    @Delete
    suspend fun delete(room: LumenRoom): Int

    @Delete
    suspend fun delete(room: List<LumenRoom>): Int
}
