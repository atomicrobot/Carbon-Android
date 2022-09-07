package com.atomicrobot.carbon.ui.lumen.scenes

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.data.lumen.dto.LumenLight
import com.atomicrobot.carbon.data.lumen.dto.LumenScene
import com.atomicrobot.carbon.data.lumen.dto.SceneAndLightsWithRoom
import com.atomicrobot.carbon.data.lumen.dto.SceneAndRoomName
import com.atomicrobot.carbon.ui.lumen.LumenIndeterminateIndicator
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@Composable
fun ScenesScreen(
    viewModel: ScenesViewModel = getViewModel(),
    onSceneSelected: (Long) -> Unit = {}
) {
    LaunchedEffect(Unit) { viewModel.getScenes() }
    val screenState by viewModel.mainUiState.collectAsState()
    var mainScreenstate = screenState.mainScreenState
    when (mainScreenstate) {
        is ScenesViewModel.Scenes.Loading -> {
            Box(Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
        is ScenesViewModel.Scenes.Result -> {
            ScenesList(
                scenes = mainScreenstate.scenes,
                modifier = Modifier.fillMaxSize(),
                onSceneSelected = onSceneSelected
            )
        }
    }
}

@Composable
fun ScenesList(
    scenes: List<SceneAndRoomName> = emptyList(),
    modifier: Modifier = Modifier.fillMaxSize(),
    onSceneSelected: (Long) -> Unit = {}
) {
    val favorites: List<SceneAndRoomName> = scenes.filter { it.scene.favorite }
    // Filter out the favorite scenes then map them into rooms so can display scenes by the
    // rooms they belong too (alphabetized)
    val sceneMap: Map<String, List<LumenScene>> = scenes
        .filter { !it.scene.favorite }
        .groupBy({ it.room.roomName }) { it.scene }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Hide the favorites section if there are none
        if (favorites.isNotEmpty()) {
            // I don't know if this is recommend but for each room we add a header as an
            // individual list item which means it can be navigated (scrolled) too by an index
            item { SceneSectionHeader(stringResource(id = R.string.favorites)) }
            items(favorites, key = { it.scene.sceneId }) {
                SceneItem(
                    scene = it.scene,
                    roomName = it.room.roomName,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSceneSelected(it.scene.sceneId) }
                ) {
                }
            }
        }
        // Enumerate the remaining rooms by scene
        for (room in sceneMap.keys) {
            // Room Section header
            item { SceneSectionHeader(room) }
            items(sceneMap[room]!!, key = { it.sceneId }) { scene ->
                SceneItem(
                    scene = scene,
                    roomName = room,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSceneSelected(scene.sceneId) }
                ) {
                }
            }
        }
    }
}

@Composable
fun AddSceneTask(
    viewModel: ScenesViewModel = getViewModel(),
    onDismissed: () -> Unit
) {
    SceneDetailsList(
            sceneId = 0L,
            viewModel = viewModel,
            newScene = true,
            onDismissed = onDismissed
    ) {
        viewModel.saveOrUpdateScene(it)
        onDismissed()
    }
}

@Composable
fun EditSceneTask(
    sceneId: Long,
    viewModel: ScenesViewModel = getViewModel(),
    onDismissed: () -> Unit
) {

    val coroutine = rememberCoroutineScope()
    SceneDetailsList(
            sceneId = sceneId,
            viewModel = viewModel,
            newScene = false,
            onDismissed = onDismissed,
            onDeleteAction = { coroutine.launch { viewModel.removeScene(sceneId) } }
    ) {
        viewModel.saveOrUpdateScene(it)
        onDismissed()
    }
}

@Composable
fun SceneDetailsList(
    sceneId: Long,
    viewModel: ScenesViewModel = getViewModel(),
    newScene: Boolean = false,
    showLoadingScrim: Boolean = true,
    onDismissed: () -> Unit = {},
    onDeleteAction: (Long) -> Unit = { },
    onSaveScene: (SceneModel) -> Unit = {},
) {
    LaunchedEffect(sceneId) {
        Log.d("DBUG", "querying db for scene id = $sceneId")
        viewModel.getScene(sceneId)
    }
    val screenState by viewModel.sceneDetailsUIState.collectAsState()
    var state: ScenesViewModel.SceneDetails = screenState.sceneDetailsState

    val rooms = when (state) {
        is ScenesViewModel.SceneDetails.LoadingDetails -> emptyList()
        is ScenesViewModel.SceneDetails.Result -> state.rooms
    }

    var scene: SceneModel by mutableStateOf(when (state) {
        is ScenesViewModel.SceneDetails.LoadingDetails -> SceneModel()
        is ScenesViewModel.SceneDetails.Result -> SceneModel(state.scene)
    })

    Box(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {
            SceneDetailsToolbar(title = scene.name, newScene = newScene, onDismissed = onDismissed)
            { onDeleteAction(sceneId) }

            if (newScene) SceneTaskDescription()

            // Load the lights available in the current room, use the roomId as the key because if the room
            // isn't updated, we don't wanna query the database again on recompositions
            LaunchedEffect(scene.roomId) { viewModel.getLightsForRoom(scene.roomId) }

            val screenState by viewModel.sceneDetailsLightUIState.collectAsState()
            val state: ScenesViewModel.SceneDetailsLights = screenState.sceneDetailsLightState

            var lights = if (state is ScenesViewModel.SceneDetailsLights.Result) state.lights
            else emptyList()

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.Top)
            ) {
                sceneDetailsField(scene, rooms = rooms) { scene = it }

                sceneDetailsLights(lights, scene.lights)

                sceneDetailsFavoriteButton(scene) { scene = it }
            }

            SceneDetailsButton(
                newScene = newScene,
                enabled = (scene.roomId != 0L && scene.name.isNotEmpty())
            ) { onSaveScene(scene) }
        }

        if (state is ScenesViewModel.SceneDetails.LoadingDetails) {
            if(showLoadingScrim) {
                Canvas(
                    Modifier
                    .fillMaxSize()
                ) {
                    drawRect(color = Color.Black, alpha = .15F)
                }
            }
            LumenIndeterminateIndicator(
                modifier = Modifier
                .size(206.dp)
                .align(Alignment.Center)
            )
        }
    }
}

@Composable
fun SceneDetailsToolbar(
    title: String,
    newScene: Boolean = false,
    onDismissed: () -> Unit = {},
    onAction: () -> Unit = {}) {
    DualActionRow(
        title = if (newScene) stringResource(id = R.string.new_scene)
        else title,
        painter = if (newScene) null
        else painterResource(id = R.drawable.ic_lumen_trash),
        actionContextDescription = if (newScene) null
        else stringResource(id = R.string.cont_desc_scene_remove),
        modifier = Modifier.fillMaxWidth(),
        onClose = onDismissed,
        onAction =  onAction
    )
}

data class SceneModel(
    val sceneId: Long = 0,
    val name: String = "",
    val favorite: Boolean = false,
    val duration: String = "",
    val roomId: Long = 0,
    val roomName: String = "",
    val lights: List<Long> = emptyList()
) {
    constructor(scene: SceneAndLightsWithRoom) : this(
        sceneId = scene.sceneId,
        name = scene.sceneName,
        favorite = scene.favorite,
        duration = scene.duration,
        roomId = scene.roomId,
        roomName = scene.roomName,
        lights = scene.lights.map { it.lightId }
    )
}
