package com.atomicrobot.carbon.ui.compose

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.atomicrobot.carbon.data.api.github.model.Commit
import com.atomicrobot.carbon.navigation.AppScreens
import com.atomicrobot.carbon.ui.main.dummyCommits

class CommitPreviewProvider : PreviewParameterProvider<Commit> {
    override val values: Sequence<Commit>
        get() = dummyCommits.asSequence()
}

class AppScreenPreviewProvider : PreviewParameterProvider<AppScreens> {
    override val values: Sequence<AppScreens>
        get() = listOf(AppScreens.Home, AppScreens.Settings).asSequence()
}
