package com.atomicrobot.carbon.data.lumen.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.atomicrobot.carbon.data.lumen.dto.LumenLight
import com.atomicrobot.carbon.data.lumen.dto.LumenScene
import com.atomicrobot.carbon.data.lumen.dto.SceneAndLightsWithRoom
import com.atomicrobot.carbon.data.lumen.dto.SceneAndRoomName
import kotlinx.coroutines.flow.Flow

@Dao
interface SceneDao {
    @Update
    suspend fun update(scene: LumenScene): Int

    @Update
    suspend fun update(scene: List<LumenScene>): Int

    @Insert
    suspend fun insert(scene: LumenScene): Long

    @Insert
    suspend fun insert(scene: List<LumenScene>): List<Long>

    @Delete
    suspend fun delete(scene: LumenScene): Int

    @Delete
    suspend fun delete(scene: List<LumenScene>): Int

    @Query("DELETE FROM LumenScene WHERE sceneId = :sceneId")
    suspend fun delete(sceneId: Long): Int

    @Query("SELECT * FROM LumenScene")
    fun getScenes(): Flow<List<LumenScene>>

    @Query("SELECT * FROM LumenScene WHERE sceneId = :sceneId")
    fun getScene(sceneId: Long): Flow<LumenScene>

    @Query(
        "SELECT * FROM LumenScene as scene " +
            "INNER JOIN LumenSceneLightCrossRef crossRef ON crossRef.sceneId = scene.sceneId " +
            "INNER JOIN LumenLight light ON light.lightId = crossRef.lightId WHERE scene.sceneId = :sceneId"
    )
    fun getSceneWithLights(sceneId: Long): Flow<Map<LumenScene, List<LumenLight>>>

    @Transaction
    @Query("SELECT * FROM LumenScene")
    fun getScenesWithRoom(): Flow<List<SceneAndRoomName>>

    @Transaction
    @Query("SELECT * FROM LumenScene WHERE sceneId = :sceneId")
    suspend fun getSceneAndLightsWithRoom(sceneId: Long): SceneAndLightsWithRoom

    @Transaction
    @Query("SELECT * FROM LumenScene WHERE sceneId = :sceneId")
    fun getSceneAndLightsWithRoomFlow(sceneId: Long): Flow<SceneAndLightsWithRoom>
}
