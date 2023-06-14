package com.atomicrobot.carbon.util

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.atomicrobot.carbon.data.api.github.model.Commit
import com.atomicrobot.carbon.data.api.github.model.DetailedCommit
import com.atomicrobot.carbon.navigation.CarbonScreens
import com.atomicrobot.carbon.navigation.LumenScreens
import com.atomicrobot.carbon.navigation.appScreens
import com.atomicrobot.carbon.navigation.lumenScreens
import com.atomicrobot.carbon.ui.main.dummyCommits
import com.atomicrobot.carbon.ui.main.dummyDetailedCommit

class CommitPreviewProvider : PreviewParameterProvider<Commit> {
    override val values: Sequence<Commit>
        get() = dummyCommits.asSequence()
}

class DetailedCommitPreviewProvider : PreviewParameterProvider<DetailedCommit> {
    override val values: Sequence<DetailedCommit>
        get() = dummyDetailedCommit.asSequence()
}

class AppScreenPreviewProvider : PreviewParameterProvider<CarbonScreens> {
    override val values: Sequence<CarbonScreens>
        get() = listOf(
            CarbonScreens.Home,
            CarbonScreens.Settings
        )
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
