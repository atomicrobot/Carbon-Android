package com.atomicrobot.carbon.ui.compose

import android.graphics.Point
import android.graphics.Rect
import androidx.camera.core.CameraSelector
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.atomicrobot.carbon.data.api.github.model.Commit
import com.atomicrobot.carbon.navigation.AppScreens
import com.atomicrobot.carbon.ui.main.dummyCommits
import com.atomicrobot.carbon.ui.scanner.BarcodeAnalysisState
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.barcode.common.internal.BarcodeSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CommitPreviewProvider : PreviewParameterProvider<Commit> {
    override val values: Sequence<Commit>
        get() = dummyCommits.asSequence()
}

class AppScreenPreviewProvider : PreviewParameterProvider<AppScreens> {
    override val values: Sequence<AppScreens>
        get() = listOf(AppScreens.Home, AppScreens.Settings, AppScreens.Scanner).asSequence()
}

class CameraSelectorProvider: PreviewParameterProvider<CameraSelector> {
    override val values: Sequence<CameraSelector>
        get() = listOf(CameraSelector
                .Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build()).asSequence()

}


