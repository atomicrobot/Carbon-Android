package com.atomicrobot.carbon.ui.lumen.scenes

import android.app.Application
import androidx.lifecycle.ViewModel
import com.atomicrobot.carbon.ui.lumen.model.RoomModel
import com.atomicrobot.carbon.ui.lumen.model.SceneModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ScenesViewModel(private val app: Application) : ViewModel() {

    val scenes: Flow<List<SceneModel>> = flow {}

    val rooms: Flow<List<RoomModel>> = flow { }

    @Suppress("UNUSED_PARAMETER")
    fun removeScene(scene: SceneModel) = Unit

    @Suppress("UNUSED_PARAMETER")
    fun saveOrUpdateScene(scene: SceneModel) = Unit

    @Suppress("UNUSED_PARAMETER")
    fun updateScene(scene: SceneModel) = Unit
}
