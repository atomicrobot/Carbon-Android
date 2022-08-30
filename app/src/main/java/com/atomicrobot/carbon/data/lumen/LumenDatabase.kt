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
import com.atomicrobot.carbon.data.lumen.dto.LumenLight
import com.atomicrobot.carbon.data.lumen.dto.LumenRoom
import com.atomicrobot.carbon.data.lumen.dto.LumenScene
import com.atomicrobot.carbon.data.lumen.dto.LumenSceneLightCrossRef
import com.atomicrobot.carbon.ui.lumen.LumenConverters
import kotlinx.coroutines.CoroutineScope
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
        const val LUMEN_DB_NAME: String = "LumenDatabase.db"

        // For Singleton instantiation
        @Volatile private var instance: LumenDatabase? = null

        fun getInstance(context: Context): LumenDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        fun buildDatabase(context: Context): LumenDatabase {
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
}

internal class LumenDatabaseCallback(val appContext: Context) : RoomDatabase.Callback() {

    private val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        applicationScope.launch {
            val roomDao = getInstance(appContext).roomDao()
            populateRoomTable(roomDao)
        }
    }

    private suspend fun populateRoomTable(roomDao: RoomDao) {
        val livingRoom = LumenRoom(roomId = 0L, name = "Living Room")
        val bedRoom = LumenRoom(roomId = 0L, name = "Master Bedroom")
        val bathRoom = LumenRoom(roomId = 0L, name = "Master Bathroom")

        var roomIds = roomDao.insert(listOf(livingRoom, bedRoom, bathRoom))
    }
}
