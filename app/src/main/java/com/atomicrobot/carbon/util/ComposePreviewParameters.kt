package com.atomicrobot.carbon.util

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.atomicrobot.carbon.data.api.github.model.Commit
import com.atomicrobot.carbon.navigation.CarbonScreens
import com.atomicrobot.carbon.navigation.LumenScreens
import com.atomicrobot.carbon.navigation.appScreens
import com.atomicrobot.carbon.navigation.lumenScreens
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

class AppScreensPreviewProvider : PreviewParameterProvider<List<CarbonScreens>> {
    override val values: Sequence<List<CarbonScreens>>
        get() = listOf(appScreens).asSequence()
}
class LumenScreensPreviewProvider : PreviewParameterProvider<List<LumenScreens>> {
    override val values: Sequence<List<LumenScreens>>
        get() = listOf(lumenScreens).asSequence()
}
