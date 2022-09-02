package com.atomicrobot.carbon.ui.lumen.scenes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.data.lumen.dto.LumenRoom
import com.atomicrobot.carbon.data.lumen.dto.LumenScene
import com.atomicrobot.carbon.data.lumen.dto.RoomNameAndId
import com.atomicrobot.carbon.data.lumen.dto.SceneAndLightsWithRoom
import com.atomicrobot.carbon.data.lumen.dto.SceneAndRoomName
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
    when(mainScreenstate) {
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
            items(sceneMap[room]!!, key = { it.sceneId}) { scene ->
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
    SceneTask(
        sceneId = 0,
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
    SceneTask(
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
fun SceneTask(
    sceneId: Long,
    viewModel: ScenesViewModel = getViewModel(),
    newScene: Boolean = false,
    onDismissed: () -> Unit = {},
    onDeleteAction: (Long) -> Unit = { },
    onSaveScene: (SceneModel) -> Unit = {},
) {
    LaunchedEffect(sceneId) { viewModel.getScene(sceneId) }

    val screenState by viewModel.sceneDetailsUIState.collectAsState()
    var state: ScenesViewModel.SceneDetails = screenState.sceneDetailsState

    Box(Modifier.fillMaxSize()) {
        var sceneData = when(state) {
            ScenesViewModel.SceneDetails.LoadingDetails -> {
                Pair(SceneModel(), emptyList())
            }
            is ScenesViewModel.SceneDetails.Result -> {
                Pair(SceneModel(state.scene), state.rooms)
            }
        }

        var scene: SceneModel = sceneData.first

        Column(Modifier.fillMaxSize()) {
            DualActionRow(
                    title = if (newScene) stringResource(id = R.string.new_scene)
                    else scene.name,
                    painter = if (newScene) null
                    else painterResource(id = R.drawable.ic_lumen_trash),
                    actionContextDescription = if (newScene) null
                    else stringResource(id = R.string.cont_desc_scene_remove),
                    modifier = Modifier.fillMaxWidth(),
                    onClose = onDismissed
            ) {
                onDeleteAction(sceneId)
            }

            if (newScene) SceneTaskDescription()

            SceneDetails(scene = scene, rooms = sceneData.second) { scene = it }

            var text =  if (newScene)
                stringResource(id = R.string.create_scene)
            else
                stringResource(id = R.string.save_scene)
            SceneTaskSaveButton(
                buttonText = text,
                enabled = (scene.roomId != 0L && scene.name.isNotEmpty()),
            ) {
            }
        }

        when(state) {
            ScenesViewModel.SceneDetails.LoadingDetails -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is ScenesViewModel.SceneDetails.Result -> {

            }
        }
    }
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
    constructor(scene: SceneAndLightsWithRoom): this(
        sceneId = scene.sceneId,
        name = scene.sceneName,
        favorite = scene.favorite,
        duration = scene.duration,
        roomId = scene.roomId,
        roomName = scene.roomName,
        lights = scene.lights.map { it.lightId }
    )
}
