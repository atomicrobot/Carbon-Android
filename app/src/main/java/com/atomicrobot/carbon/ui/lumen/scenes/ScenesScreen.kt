package com.atomicrobot.carbon.ui.lumen.scenes

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.data.lumen.Room
import com.atomicrobot.carbon.data.lumen.Scene
import com.atomicrobot.carbon.ui.compose.ScenePreviewProvider
import com.atomicrobot.carbon.ui.compose.ScenesPreviewProvider
import com.atomicrobot.carbon.ui.main.dummyRooms
import com.atomicrobot.carbon.ui.main.dummyScenes
import com.atomicrobot.carbon.ui.shader.AngledLinearGradient
import com.atomicrobot.carbon.ui.theme.LightBlurple
import com.atomicrobot.carbon.ui.theme.White3
import com.atomicrobot.carbon.ui.theme.White50
import org.koin.androidx.compose.getViewModel

@Preview
@Composable
fun ScenesScreen(onSceneSelected: (Scene) -> Unit = {}) {
    ScenesList(
        scenes = dummyScenes,
        modifier = Modifier.fillMaxSize(),
        onSceneSelected = onSceneSelected
    )
}

@Preview
@Composable
fun ScenesList(
    @PreviewParameter(ScenesPreviewProvider::class, limit = 1) scenes: List<Scene>,
    modifier: Modifier = Modifier.fillMaxSize(),
    onSceneSelected: (Scene) -> Unit = {}
) {
    // Extract the favorite scenes because they will be displayed in a section at the top of the
    // list
    val favorites = remember {
        scenes.filter { scene -> scene.favorite }
    }

    // Filter out the favorite scenes then map them into rooms so can display scenes by the rooms
    // they belong too (alphabetized)
    val rooms = remember {
        scenes
            .filter { scene -> !scene.favorite }
            .map { scene -> scene.room }
            .distinct()
            .sortedBy { it.name }
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
            items(favorites) { scene ->
                SceneItem(
                    scene = scene,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSceneSelected(scene) }
                )
            }
        }

        for (room in rooms) {
            item {
                SceneSectionHeader(room.name)
            }
            items(room.scenes) { scene ->
                SceneItem(
                    scene = scene,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSceneSelected(scene) }
                )
            }
        }
    }
}

@Composable
fun AddSceneTask(onTaskComplete: () -> Unit = {}) {
    AddSceneTask(onTaskComplete = onTaskComplete) {
    }
}

@Preview
@Composable
fun AddSceneTask(
    onTaskComplete: () -> Unit = {},
    onSaveScene: (Scene) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        DualActionRow(
            title = stringResource(id = R.string.new_scene),
            actionContextDescription = stringResource(id = R.string.delete_scene),
            onClose = onTaskComplete
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.Top)
        ) {
        }

        Button(
            onClick = { },
            modifier = Modifier
                .border(
                    width = 1.dp,
                    brush = AngledLinearGradient(
                        colors = listOf(White50, White3),
                        angleInDegrees = -135F,
                        useAsCssAngle = true
                    ),
                    shape = MaterialTheme.shapes.small
                )
                .fillMaxWidth()
                .height(68.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = LightBlurple)
        ) {
            Text(
                text = stringResource(id = R.string.save_scene),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h2
            )
        }
    }
}

@Composable
fun EditSceneTask(
    scene: Scene,
    onTaskComplete: () -> Unit = {}
) {
    val viewModel: ScenesViewModel = getViewModel()
    EditScene(
        scene = scene,
        onTaskComplete = onTaskComplete,
        onDeleteScene = viewModel::removeScene,
        onSaveScene = viewModel::removeScene
    )
}

@Preview
@Composable
fun EditScene(
    @PreviewParameter(ScenePreviewProvider::class, limit = 1) scene: Scene,
    onTaskComplete: () -> Unit = {},
    onDeleteScene: (Scene) -> Unit = {},
    onSaveScene: (Scene) -> Unit = {},
) {
    var tempScene by remember { mutableStateOf(scene.copy()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        DualActionRow(
            title = tempScene.name,
            painter = painterResource(id = R.drawable.ic_lumen_trash),
            actionContextDescription = stringResource(id = R.string.delete_scene),
            onClose = onTaskComplete
        ) {
            onDeleteScene(tempScene)
            // Task is complete
            onTaskComplete()
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.Top)
        ) {
            item {
                TaskLabeledTextField(
                    label = stringResource(id = R.string.name),
                    text = tempScene.name,
                    placeholder = stringResource(id = R.string.name_room)
                )

                TaskLabeledDropDownMenu(
                    label = stringResource(id = R.string.room),
                    options = dummyRooms,
                    selectedOption = tempScene.room,
                    placeholder = stringResource(id = R.string.select_room),
                    modifier = Modifier.padding(vertical = 16.dp)
                ) {
                    tempScene = tempScene.copy(room = it as Room)
                }

                TaskLabeledDropDownMenu(
                    label = stringResource(id = R.string.duration),
                    options = stringArrayResource(id = R.array.durations)
                        .toList(),
                    selectedOption = tempScene.duration,
                    placeholder = stringResource(id = R.string.select_durations),
                ) {
                    tempScene = tempScene.copy(duration = it as String)
                }
            }

            item {
                SceneSectionHeader(stringResource(id = R.string.lights))
            }
            items(scene.devices) {
                SceneDeviceItem(it, modifier = Modifier.fillMaxWidth())
            }
        }

        Button(
            onClick = { onSaveScene(tempScene) },
            modifier = Modifier
                .border(
                    width = 1.dp,
                    brush = AngledLinearGradient(
                        colors = listOf(White50, White3),
                        angleInDegrees = -135F,
                        useAsCssAngle = true
                    ),
                    shape = MaterialTheme.shapes.small
                )
                .fillMaxWidth()
                .height(68.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = LightBlurple)
        ) {
            Text(
                text = stringResource(id = R.string.save_scene),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h2
            )
        }
    }
}