package com.atomicrobot.carbon.ui.lumen.scenes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atomicrobot.carbon.data.lumen.dao.SceneDao
import com.atomicrobot.carbon.data.lumen.dto.LumenScene
import com.atomicrobot.carbon.data.lumen.dto.RoomNameAndId
import com.atomicrobot.carbon.data.lumen.dto.SceneAndLightsWithRoom
import com.atomicrobot.carbon.data.lumen.dto.SceneAndRoomName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ScenesViewModel(
    private val sceneDao: SceneDao,
) : ViewModel() {

    sealed class Scenes {
        object Loading : Scenes()
        class Result(val scenes: List<SceneAndRoomName>) : Scenes()
    }

    sealed class SceneTask {
        object LoadingScene: SceneTask()
        class Result(val scene: SceneAndLightsWithRoom): SceneTask()
    }

    data class MainScenesScreenUiState(
        val sceneTaskState: SceneTask = SceneTask.Result(
            SceneAndLightsWithRoom(
                scene = LumenScene(),
                lights = emptyList(),
                room = RoomNameAndId()
            )
        ),
        val mainScreenState: Scenes = Scenes.Result(emptyList())
    )

    private val _mainUiState = MutableStateFlow(MainScenesScreenUiState())
    val mainUiState: StateFlow<MainScenesScreenUiState>
        get() = _mainUiState

    suspend fun getScenes() {
        // Update the UI state to indicate that we are loading.
        _mainUiState.value = _mainUiState.value.copy(mainScreenState = Scenes.Loading)
        viewModelScope.launch {
            sceneDao.getScenesWithRoom().collect {
                _mainUiState.value = _mainUiState.value.copy(mainScreenState = Scenes.Result(it))
            }
        }
    }

    suspend fun getScene(sceneId: Long) {
        if(sceneId == 0L) {
            _mainUiState.value = _mainUiState.value.copy(
                sceneTaskState = SceneTask.Result(
                    SceneAndLightsWithRoom(
                        scene = LumenScene(),
                        lights = emptyList(),
                        room = RoomNameAndId()
                    )
                )
            )
            return
        }

        _mainUiState.value = _mainUiState.value.copy(sceneTaskState = SceneTask.LoadingScene)
        viewModelScope.launch {
            sceneDao.getSceneAndLightsWithRoom(sceneId).collect {
                _mainUiState.value = _mainUiState.value.copy(sceneTaskState = SceneTask.Result(it))
            }
        }
    }

    suspend fun removeScene(sceneId: Long) {
        viewModelScope.launch(Dispatchers.IO) { sceneDao.delete(sceneId) }
    }

    @Suppress("UNUSED_PARAMETER")
    fun saveOrUpdateScene(scene: SceneModel) = Unit

    @Suppress("UNUSED_PARAMETER")
    fun updateScene(scene: SceneModel) = Unit
}
