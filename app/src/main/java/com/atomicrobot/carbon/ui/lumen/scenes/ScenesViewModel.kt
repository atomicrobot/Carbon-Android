package com.atomicrobot.carbon.ui.lumen.scenes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atomicrobot.carbon.data.lumen.SceneModel
import com.atomicrobot.carbon.data.lumen.dao.LightDao
import com.atomicrobot.carbon.data.lumen.dao.RoomDao
import com.atomicrobot.carbon.data.lumen.dao.SceneDao
import com.atomicrobot.carbon.data.lumen.dao.SceneLightDao
import com.atomicrobot.carbon.data.lumen.dto.LumenLight
import com.atomicrobot.carbon.data.lumen.dto.LumenScene
import com.atomicrobot.carbon.data.lumen.dto.LumenSceneLightCrossRef
import com.atomicrobot.carbon.data.lumen.dto.RoomNameAndId
import com.atomicrobot.carbon.data.lumen.dto.SceneAndLightsWithRoom
import com.atomicrobot.carbon.data.lumen.dto.SceneAndRoomName
import com.atomicrobot.carbon.data.lumen.toLumenScene
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ScenesViewModel(
    private val sceneDao: SceneDao,
    private val lightDao: LightDao,
    private val roomDao: RoomDao,
    private val sceneLightDao: SceneLightDao
) : ViewModel() {

    sealed class Scenes {
        object Loading : Scenes()
        class Result(val scenes: List<SceneAndRoomName>) : Scenes()
    }

    sealed class SceneDetails {
        object LoadingDetails : SceneDetails()
        data class Result(
            val scene: SceneAndLightsWithRoom =
                SceneAndLightsWithRoom(
                    scene = LumenScene(),
                    lights = emptyList(),
                    room = RoomNameAndId()
                ),
            val rooms: List<RoomNameAndId> = emptyList()
        ) : SceneDetails()
    }

    sealed class SceneDetailsLights {
        object LoadingLights : SceneDetailsLights()
        data class Result(
            val lights: List<LumenLight> = emptyList()
        ) : SceneDetailsLights()
    }

    data class MainScenesScreenUiState(
        val mainScreenState: Scenes = Scenes.Result(emptyList())
    )

    data class SceneDetailsUIState(
        val sceneDetailsState: SceneDetails = SceneDetails.Result(),
    )

    data class SceneDetailsLightsUIState(
        val sceneDetailsLightState: SceneDetailsLights = SceneDetailsLights.Result(),
    )

    private val _mainUiState = MutableStateFlow(MainScenesScreenUiState())
    val mainUiState: StateFlow<MainScenesScreenUiState>
        get() = _mainUiState

    private val _sceneDetailsUIState = MutableStateFlow(SceneDetailsUIState())
    val sceneDetailsUIState: StateFlow<SceneDetailsUIState>
        get() = _sceneDetailsUIState

    private val _sceneDetailsLightUIState = MutableStateFlow(SceneDetailsLightsUIState())
    val sceneDetailsLightUIState: StateFlow<SceneDetailsLightsUIState>
        get() = _sceneDetailsLightUIState

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
        if (sceneId == 0L) {
            viewModelScope.launch {
                _sceneDetailsUIState.value =
                    _sceneDetailsUIState.value.copy(
                        SceneDetails.Result(
                            scene = SceneAndLightsWithRoom(
                                scene = LumenScene(),
                                lights = emptyList(),
                                room = RoomNameAndId()
                            ),
                            rooms = roomDao.getRoomNamesAndIds()
                        )
                    )
            }
            return
        }

        _sceneDetailsUIState.value =
            _sceneDetailsUIState.value.copy(sceneDetailsState = SceneDetails.LoadingDetails)
        viewModelScope.launch {
            val scene = sceneDao.getSceneAndLightsWithRoom(sceneId)
            val rooms = roomDao.getRoomNamesAndIds()
            _sceneDetailsUIState.value =
                _sceneDetailsUIState.value.copy(sceneDetailsState = SceneDetails.Result(scene, rooms))
        }
    }

    suspend fun getLightsForRoom(roomId: Long) {
        if (roomId == 0L) {
            // Invalid room ID, use an empty light list for the state
            _sceneDetailsLightUIState.value = _sceneDetailsLightUIState.value.copy(
                sceneDetailsLightState = SceneDetailsLights.Result(emptyList())
            )
        }

        _sceneDetailsLightUIState.value = _sceneDetailsLightUIState.value.copy(
            sceneDetailsLightState = SceneDetailsLights.LoadingLights
        )
        viewModelScope.launch {
            lightDao.getAllLightsForRoom(roomId).collect {
                _sceneDetailsLightUIState.value = _sceneDetailsLightUIState.value.copy(
                    sceneDetailsLightState = SceneDetailsLights.Result(it)
                )
            }
        }
    }

    fun resetSceneDetailsState() {
        // Initialize the details state to loading...
        _sceneDetailsUIState.value =
            _sceneDetailsUIState.value
                .copy(sceneDetailsState = SceneDetails.LoadingDetails)
        _sceneDetailsLightUIState.value =
            _sceneDetailsLightUIState.value
                .copy(sceneDetailsLightState = SceneDetailsLights.LoadingLights)
    }

    suspend fun removeScene(sceneId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            sceneDao.delete(sceneId)
        }
    }

    suspend fun saveOrUpdateScene(scene: SceneModel) {
        viewModelScope.launch(Dispatchers.IO) {
            val sceneId: Long = if (scene.sceneId < 1) {
                // Insert scene
                sceneDao.insert(scene.toLumenScene())
            } else {
                sceneDao.update(scene.toLumenScene())
                scene.sceneId
            }
            // Get a list of the lights in the scene so we can detect which lights were added, and
            // which lights were removed
            val sceneLights = sceneDao.getSceneLightReferences(scene.sceneId)
            // Get the removed lights
            val rmvLights = sceneLights.filterNot { scene.lights.contains(it.lightId) }
            sceneLightDao.delete(rmvLights)
            val newLights = scene.lights
                .filter { lightId -> !sceneLights.any { it.sceneId == lightId } }
                .map { LumenSceneLightCrossRef(sceneId = sceneId, lightId = it) }
            sceneLightDao.insert(newLights)
        }
    }
}
