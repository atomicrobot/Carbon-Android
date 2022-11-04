package com.atomicrobot.carbon.data.lumen.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import com.atomicrobot.carbon.data.lumen.dto.LumenSceneLightCrossRef

@Dao
interface SceneLightDao {
    @Update
    suspend fun update(room: LumenSceneLightCrossRef): Int

    @Update
    suspend fun update(room: List<LumenSceneLightCrossRef>): Int

    @Insert
    suspend fun insert(room: LumenSceneLightCrossRef): Long

    @Insert
    suspend fun insert(room: List<LumenSceneLightCrossRef>): List<Long>

    @Delete
    suspend fun delete(room: LumenSceneLightCrossRef): Int

    @Delete
    suspend fun delete(room: List<LumenSceneLightCrossRef>): Int
}
