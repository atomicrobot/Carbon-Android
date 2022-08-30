package com.atomicrobot.carbon.data.lumen.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.atomicrobot.carbon.data.lumen.dto.LumenRoom
import com.atomicrobot.carbon.data.lumen.dto.LumenRoomAndAllScenes
import com.atomicrobot.carbon.data.lumen.dto.LumenRoomAndAllScenesWithLights
import com.atomicrobot.carbon.ui.lumen.model.RoomModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Dao
interface RoomDao {

    @Update
    fun update(room: LumenRoom): Int

    @Update
    fun update(room: List<LumenRoom>): Int

    @Insert
    fun insert(room: LumenRoom): Long

    @Insert
    fun insert(room: List<LumenRoom>): List<Long>

    @Delete
    fun delete(room: LumenRoom): Int

    @Delete
    fun delete(room: List<LumenRoom>): Int

    @Transaction
    @Query("SELECT * FROM LumenRoom")
    fun getRoomsWithScenes(): Flow<List<LumenRoomAndAllScenes>>

    @Transaction
    @Query("SELECT * FROM LumenRoom")
    fun getRoomsWithSceneAndLights(): Flow<List<LumenRoomAndAllScenesWithLights>>

    @Query("SELECT * FROM LumenRoom")
    fun getAllRooms(): Flow<List<LumenRoom>>

    fun getRooms(): Flow<List<RoomModel>> = getRoomsWithSceneAndLights().map { it.map { RoomModel(it) } }
}
