package com.atomicrobot.carbon.ui.compose

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.atomicrobot.carbon.data.api.github.model.Commit
import com.atomicrobot.carbon.navigation.CarbonScreens
import com.atomicrobot.carbon.navigation.LumenScreens
import com.atomicrobot.carbon.navigation.appScreens
import com.atomicrobot.carbon.navigation.lumenScreens
import com.atomicrobot.carbon.ui.lumen.model.RoomModel
import com.atomicrobot.carbon.ui.lumen.model.SceneModel
import com.atomicrobot.carbon.ui.main.dummyCommits

class CommitPreviewProvider : PreviewParameterProvider<Commit> {
    override val values: Sequence<Commit>
        get() = dummyCommits.asSequence()
}

class AppScreenPreviewProvider : PreviewParameterProvider<CarbonScreens> {
    override val values: Sequence<CarbonScreens>
        get() = listOf(CarbonScreens.Home, CarbonScreens.Settings, CarbonScreens.Scanner)
            .asSequence()
}

class ScenePreviewProvider : PreviewParameterProvider<SceneModel> {
    override val values: Sequence<SceneModel>
        get() {
            val room = RoomModel(roomName = "Living Room")
            val grow = SceneModel(
                containingRoom = room,
                name = "Grow Lights",
                duration = "Manual"
            )
            val movieNight = SceneModel(
                containingRoom = room,
                name = "Movie Night",
                duration = "3hr"
            )
            val reading = SceneModel(
                containingRoom = room,
                name = "Reading",
                duration = "45min left"
            )
            return listOf(grow, movieNight, reading).asSequence()
        }
}

class ScenesPreviewProvider : PreviewParameterProvider<List<SceneModel>> {
    override val values: Sequence<List<SceneModel>>
        get() = listOf<List<SceneModel>>().asSequence()
}

class AppScreensPreviewProvider : PreviewParameterProvider<List<CarbonScreens>> {
    override val values: Sequence<List<CarbonScreens>>
        get() = listOf(appScreens).asSequence()
}
class LumenScreensPreviewProvider : PreviewParameterProvider<List<LumenScreens>> {
    override val values: Sequence<List<LumenScreens>>
        get() = listOf(lumenScreens).asSequence()
}
