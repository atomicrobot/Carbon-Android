package com.atomicrobot.carbon.data.lumen.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.atomicrobot.carbon.data.lumen.dto.LumenRoomAndAllScenes
import com.atomicrobot.carbon.data.lumen.dto.LumenScene
import com.atomicrobot.carbon.data.lumen.dto.SceneWithLights
import kotlinx.coroutines.flow.Flow

@Dao
interface SceneDao {
    @Update
    fun update(scene: LumenScene): Int

    @Update
    fun update(scene: List<LumenScene>): Int

    @Insert
    fun insert(scene: LumenScene): Long

    @Insert
    fun insert(scene: List<LumenScene>): List<Long>

    @Delete
    fun delete(scene: LumenScene): Int

    @Delete
    fun delete(scene: List<LumenScene>): Int

    @Query("SELECT * FROM LumenScene WHERE favorite = 1")
    fun getFavoriteScenes(): Flow<List<LumenScene>>

    @Transaction
    @Query("SELECT * FROM LumenScene WHERE favorite = 1")
    fun getFavoriteScenesWithLights(): Flow<List<SceneWithLights>>

    @Transaction
    @Query("SELECT * FROM LumenScene")
    fun getScenesWithLights(): Flow<List<SceneWithLights>>

    @Query("SELECT * FROM LumenScene")
    fun getAllScenes(): Flow<List<LumenScene>>

    @Query("SELECT * FROM LumenScene WHERE containingRoomId = :roomId")
    fun getRoomScenes(roomId: Long): Flow<List<LumenScene>>
}
