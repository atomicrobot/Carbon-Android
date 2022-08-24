package com.atomicrobot.carbon.ui.lumen.scenes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.data.lumen.Scene
import com.atomicrobot.carbon.ui.compose.ScenePreviewProvider
import com.atomicrobot.carbon.ui.compose.ScenesPreviewProvider
import com.atomicrobot.carbon.ui.main.dummyScenes
import com.atomicrobot.carbon.ui.shader.AngledLinearGradient
import com.atomicrobot.carbon.ui.theme.CardBackgroundOff
import com.atomicrobot.carbon.ui.theme.CardBackgroundOn
import com.atomicrobot.carbon.ui.theme.White3
import com.atomicrobot.carbon.ui.theme.White50
import org.koin.androidx.compose.getViewModel

@Preview
@Composable
fun ScenesScreen(
        viewModel: ScenesViewModel = getViewModel(),
        onSceneSelected: (Scene) -> Unit = {}) {
    val scenes by viewModel.scenes.collectAsState(initial = dummyScenes)
    ScenesList(
            scenes = scenes,
            modifier = Modifier.fillMaxSize(),
            onSceneSelected = onSceneSelected)
}

@Preview
@Composable
fun ScenesList(
        @PreviewParameter(ScenesPreviewProvider::class, limit = 1) scenes: List<Scene>,
        modifier: Modifier = Modifier.fillMaxSize(),
        onSceneSelected: (Scene) -> Unit = {}
) {
    val favorites = remember {
        scenes.filter { scene -> scene.favorite }
    }
    // Strip out favorites
    val rooms = remember {
        scenes
            .filter { scene -> !scene.favorite }
            .map { scene -> scene.room }
                .distinct()
    }

    LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)) {

        if(favorites.isNotEmpty()) {
            item {
                SceneSectionHeader("Favorites")
            }
            items(favorites) { scene ->
                SceneItem(
                    scene = scene,
                    modifier = Modifier.fillMaxWidth().clickable { onSceneSelected(scene) })
            }
            for(room in rooms) {
                item {
                    SceneSectionHeader(room.name)
                }
                items(room.scenes) { scene ->
                    SceneItem(
                        scene = scene,
                        modifier = Modifier.fillMaxWidth().clickable { onSceneSelected(scene) })
                }
            }
        }
    }
}

@Preview
@Composable
fun SceneSectionHeader(
        headerTitle: String = "Favorites",
        modifier: Modifier = Modifier
                .fillMaxWidth()
) {
    Text(
        text = headerTitle,
        modifier = modifier
                .fillMaxWidth()
                .padding(top = 14.dp),
        color = Color.White,
        style = MaterialTheme.typography.h2)
}

@Preview
@Composable
fun SceneItem(
    @PreviewParameter(ScenePreviewProvider::class, limit = 2) scene: Scene,
    modifier: Modifier = Modifier.fillMaxWidth(),
) {
    Row(
        modifier = modifier
            .border(
                width = 1.dp,
                brush = AngledLinearGradient(
                        colors = listOf(White50, White3),
                        angleInDegrees = -135F,
                        useAsCssAngle = true),
                shape = RoundedCornerShape(8.dp))
            .background(
                color = if(scene.active) CardBackgroundOn else CardBackgroundOff,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically) {
            Column(
                modifier = Modifier.weight(1f)) {
                Text(
                    text = scene.name,
                    modifier = Modifier
                            .padding(bottom = 8.dp),
                    color = Color.White,
                    style = MaterialTheme.typography.h3
                )
                // Since the non-favorite Scenes are enumerated by the containing 'room',
                // we only need to show the 'room' label for the favorite Scenes
                if(scene.favorite)
                    Text(
                        text = scene.room.name,
                        modifier = Modifier
                                .padding(bottom = 4.dp),
                        color = Color.White,
                        style = MaterialTheme.typography.body1
                    )

                DurationLabel(scene.duration, scene.active)
            }
            val imgData = if(scene.active)
                Pair(R.drawable.ic_lumen_stop_filled, R.string.cont_desc_stop_scene)
            else
                Pair(R.drawable.ic_lumen_play, R.string.cont_desc_start_scene)

            Image(
                painter = painterResource(imgData.first),
                contentDescription = stringResource(imgData.second),
                modifier = Modifier.size(48.dp),
                colorFilter = ColorFilter.tint(Color.White)
            )
        }
    }
}

@Preview
@Composable
fun DurationLabel(
        duration: String = "6Hr",
        active: Boolean = false,
        modifier: Modifier = Modifier
                .fillMaxWidth()) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(id = if(active)
                R.drawable.ic_lumen_timer
            else
                R.drawable.ic_lumen_clock),
            contentDescription = "Duration Icon",
            modifier = Modifier
                .size(24.dp)
                .padding(end = 8.dp),
            tint = Color.White)
        Text(
            text = duration,
            color = Color.White,
            style = MaterialTheme.typography.body1)
    }
}