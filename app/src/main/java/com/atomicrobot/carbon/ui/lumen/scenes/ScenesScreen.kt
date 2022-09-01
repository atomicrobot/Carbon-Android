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
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.data.lumen.dto.LumenScene
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
    val favorites: List<SceneAndRoomName> = remember {
        scenes.filter { it.scene.favorite }
    }
    // Filter out the favorite scenes then map them into rooms so can display scenes by the
    // rooms they belong too (alphabetized)
    val sceneMap: Map<String, List<LumenScene>> = remember {
        scenes
            .filter { !it.scene.favorite }
            .groupBy({ it.room.roomName }) { it.scene }
    }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Don't at a sections header if there are no favorites
        if (favorites.isNotEmpty()) {
            // I don't know if this is recommend but for each room we add a header as an
            // individual list item which means it can be navigated (scrolled) too by an index
            item {
                SceneSectionHeader(stringResource(id = R.string.favorites))
            }
            items(favorites, key = { it.scene.sceneId}) { it ->
                SceneItem(
                    scene = it.scene,
                    roomName = it.room.roomName,
                    modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSceneSelected(it.scene.sceneId) }
                )
            }
        }

        for (room in sceneMap.keys) {
            item {
                SceneSectionHeader(room)
            }
            items(sceneMap[room]!!, key = { it.sceneId}) { scene ->
                SceneItem(
                        scene = scene,
                        roomName = room,
                        modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSceneSelected(scene.sceneId) }
                )
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
    LaunchedEffect(Unit) { viewModel.getScene(sceneId) }
    val screenState by viewModel.mainUiState.collectAsState()
    var taskState: ScenesViewModel.SceneTask = screenState.sceneTaskState
    when(taskState) {
        ScenesViewModel.SceneTask.LoadingScene -> {
            Box(Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
        is ScenesViewModel.SceneTask.Result -> {
            val scene by remember { mutableStateOf(SceneModel(taskState.scene)) }

            Column(Modifier.fillMaxSize()) {
                DualActionRow(
                    title = scene.name,
                    painter = if (newScene) null
                    else painterResource(id = R.drawable.ic_lumen_trash),
                    actionContextDescription = if (newScene) null
                    else stringResource(id = R.string.cont_desc_scene_remove),
                    onClose = onDismissed
                ) {
                    onDeleteAction(sceneId)
                }

                if (newScene) SceneTaskDescription()

//                SceneTaskList(scene) {
//
//                }

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
        }
    }
}

data class SceneModel(
    val sceneId: Long = 0,
    val name: String = "New Scene",
    val favorite: Boolean = false,
    val roomId: Long = 0,
    val lights: List<Long> = emptyList()
) {
    constructor(scene: SceneAndLightsWithRoom): this(
        sceneId = scene.scene.sceneId,
        name = scene.scene.sceneName,
        favorite = scene.scene.favorite,
        roomId = scene.scene.containingRoomId,
        lights = scene.lights.map { it.lightId }
    ) {}
}
