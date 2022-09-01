package com.atomicrobot.carbon.data.lumen.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.atomicrobot.carbon.data.lumen.dto.LumenLight
import kotlinx.coroutines.flow.Flow

@Dao
interface LightDao {
    @Update
    suspend fun update(light: LumenLight): Int

    @Update
    suspend fun update(light: List<LumenLight>): Int

    @Insert
    suspend fun insert(light: LumenLight): Long

    @Insert
    suspend fun insert(light: List<LumenLight>): List<Long>

    @Delete
    suspend fun delete(light: LumenLight): Int

    @Delete
    suspend fun delete(light: List<LumenLight>): Int

    @Query("SELECT * FROM LumenLight")
    fun getAllLights(): Flow<List<LumenLight>>

    @Query(
        "SELECT lumenLight.* FROM lumenLight " +
            "INNER JOIN LumenSceneLightCrossRef sceneLight ON sceneLight.lightId = lumenLight.lightId " +
            "WHERE sceneLight.sceneId = :sceneId"
    )
    fun getAllLightsForScene(sceneId: Long): Flow<List<LumenLight>>

    @Query("SELECT * FROM LumenLight WHERE containingRoomId = :roomId")
    fun getAllLightsForRoom(roomId: Long): Flow<List<LumenLight>>
}
