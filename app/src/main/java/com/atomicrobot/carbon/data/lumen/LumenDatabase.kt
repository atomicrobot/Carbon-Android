package com.atomicrobot.carbon.data.lumen

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.atomicrobot.carbon.data.lumen.LumenDatabase.Companion.LUMEN_DB_VERSIONS
import com.atomicrobot.carbon.data.lumen.LumenDatabase.Companion.getInstance
import com.atomicrobot.carbon.data.lumen.dao.LightDao
import com.atomicrobot.carbon.data.lumen.dao.RoomDao
import com.atomicrobot.carbon.data.lumen.dao.SceneDao
import com.atomicrobot.carbon.data.lumen.dao.SceneLightDao
import com.atomicrobot.carbon.data.lumen.dto.LightType
import com.atomicrobot.carbon.data.lumen.dto.LumenLight
import com.atomicrobot.carbon.data.lumen.dto.LumenRoom
import com.atomicrobot.carbon.data.lumen.dto.LumenScene
import com.atomicrobot.carbon.data.lumen.dto.LumenSceneLightCrossRef
import com.atomicrobot.carbon.ui.lumen.LumenConverters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@Database(
    entities = [
        LumenRoom::class,
        LumenScene::class,
        LumenLight::class,
        LumenSceneLightCrossRef::class
    ],
    version = LUMEN_DB_VERSIONS,
    exportSchema = false
)
@TypeConverters(LumenConverters::class)
abstract class LumenDatabase : RoomDatabase() {
    companion object {

        const val LUMEN_DB_VERSIONS: Int = 1
        private const val LUMEN_DB_NAME: String = "LumenDatabase.db"

        // For Singleton instantiation
        @Volatile private var instance: LumenDatabase? = null

        fun getInstance(context: Context): LumenDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): LumenDatabase {
            return Room.databaseBuilder(
                context,
                LumenDatabase::class.java,
                LUMEN_DB_NAME
            )
                .addCallback(LumenDatabaseCallback(context.applicationContext))
                .build()
        }
    }

    abstract fun roomDao(): RoomDao

    abstract fun sceneDao(): SceneDao

    abstract fun lightDao(): LightDao

    abstract fun sceneLightDao(): SceneLightDao
}

internal class LumenDatabaseCallback(private val appContext: Context) : RoomDatabase.Callback() {

    private val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        applicationScope.launch(Dispatchers.IO) { populateLumenDatabase(getInstance(appContext)) }
    }

    private suspend fun populateLumenDatabase(db: LumenDatabase) {
        val roomDao = db.roomDao()
        val lightDao = db.lightDao()
        val sceneDao = db.sceneDao()
        val sceneLightDao = db.sceneLightDao()

        val livingRoomId = roomDao.insert(LumenRoom(roomId = 0L, roomName = "Living Room"))
        // Insert the living room lights and use the Ids to create scene(s)
        val livingRoomIds = lightDao.insert(listOf(
            LumenLight(lightName = "TV Lamp L", containingRoomId = livingRoomId, type = LightType.WHITE_SMALL),
            LumenLight(lightName = "TV Lamp R", containingRoomId = livingRoomId, type = LightType.WHITE_SMALL),
            LumenLight(lightName = "Coffee Table", containingRoomId = livingRoomId, type = LightType.WHITE)
        ))
        // Create some initial scenes
        var sceneId = sceneDao.insert(LumenScene(sceneName = "Movie Night", containingRoomId = livingRoomId, duration = "8 hours", favorite = true))
        sceneLightDao.insert(LumenSceneLightCrossRef(sceneId = sceneId, lightId = livingRoomIds[0]))
        sceneLightDao.insert(LumenSceneLightCrossRef(sceneId = sceneId, lightId = livingRoomIds[1]))

        sceneId = sceneDao.insert(LumenScene(sceneName = "Couch Reading", containingRoomId = livingRoomId, duration = "1 hour", favorite = false))
        sceneLightDao.insert(LumenSceneLightCrossRef(sceneId = sceneId, lightId = livingRoomIds[2]))


        val bedRoomId = roomDao.insert(LumenRoom(roomId = 0L, roomName = "Master Bedroom"))
        val bedroomIds = lightDao.insert(listOf(
            LumenLight(lightName = "Floor Lamp", containingRoomId = bedRoomId, type = LightType.COLOR),
            LumenLight(lightName = "Ceiling Fan", containingRoomId = bedRoomId, type = LightType.WHITE),
            LumenLight(lightName = "Night Stand R", containingRoomId = bedRoomId, type = LightType.WHITE_SMALL),
            LumenLight(lightName = "Night Stand L", containingRoomId = bedRoomId, type = LightType.WHITE_SMALL),
            LumenLight(lightName = "Closet Light", containingRoomId = bedRoomId, type = LightType.WHITE_SMALL),
        ))

        sceneId = sceneDao.insert(LumenScene(sceneName = "Night Light", containingRoomId = bedRoomId, duration = "8 hours", favorite = false))
        sceneLightDao.insert(LumenSceneLightCrossRef(sceneId = sceneId, lightId = bedroomIds.last()))

        sceneId = sceneDao.insert(LumenScene(sceneName = "Reading", containingRoomId = bedRoomId, duration = "1 hour", favorite = false))
        sceneLightDao.insert(LumenSceneLightCrossRef(sceneId = sceneId, lightId = bedroomIds[2]))
        sceneLightDao.insert(LumenSceneLightCrossRef(sceneId = sceneId, lightId = bedroomIds[3]))

        val bathRooomId = roomDao.insert(LumenRoom(roomId = 0L, roomName = "Master Bathroom"))
        val initialBathroomLights = listOf(
            LumenLight(lightName = "Mirror", containingRoomId = bathRooomId, type = LightType.COLOR),
            LumenLight(lightName = "Mirror", containingRoomId = bathRooomId, type = LightType.COLOR),
            LumenLight(lightName = "Mirror", containingRoomId = bathRooomId, type = LightType.COLOR),
        )
        lightDao.insert(initialBathroomLights)
    }
}
