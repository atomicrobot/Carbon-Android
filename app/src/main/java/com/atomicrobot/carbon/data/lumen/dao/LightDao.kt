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
    fun update(light: LumenLight): Int

    @Update
    fun update(light: List<LumenLight>): Int

    @Insert
    fun insert(light: LumenLight): Long

    @Insert
    fun insert(light: List<LumenLight>): List<Long>

    @Delete
    fun delete(light: LumenLight): Int

    @Delete
    fun delete(light: List<LumenLight>): Int

    @Query("SELECT * FROM LumenLight")
    fun getAllLights(): Flow<List<LumenLight>>

    @Query("SELECT lumenLight.* FROM lumenLight " +
            "INNER JOIN LumenSceneLightCrossRef sceneLight ON sceneLight.lightId = lumenLight.lightId " +
            "WHERE sceneLight.sceneId = :sceneId")
    fun getAllLightsForScene(sceneId: Long): Flow<List<LumenLight>>

    @Query("SELECT * FROM LumenLight WHERE containingRoomId = :roomId")
    fun getAllLightsForRoom(roomId: Long): Flow<List<LumenLight>>
}
