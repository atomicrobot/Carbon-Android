package com.atomicrobot.carbon.ui.lumen.scenes

import android.app.Application
import androidx.lifecycle.ViewModel
import com.atomicrobot.carbon.data.lumen.Room
import com.atomicrobot.carbon.data.lumen.Scene
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ScenesViewModel(private val app: Application) : ViewModel() {

    val scenes: Flow<List<Scene>> = flow {}

    val rooms: Flow<List<Room>> = flow { }

    @Suppress("UNUSED_PARAMETER")
    fun removeScene(scene: Scene) = Unit

    @Suppress("UNUSED_PARAMETER")
    fun updateScene(scene: Scene) = Unit
}
