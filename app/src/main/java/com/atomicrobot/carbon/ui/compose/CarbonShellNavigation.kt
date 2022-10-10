package com.atomicrobot.carbon.ui.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BackdropScaffold
import androidx.compose.material.BackdropScaffoldState
import androidx.compose.material.BackdropValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberBackdropScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.ui.theme.CarbonShellTheme
import com.atomicrobot.carbon.ui.theme.Neutron
import com.atomicrobot.carbon.ui.theme.White100
import com.atomicrobot.carbon.ui.theme.carbonShapes

sealed class CarbonShellProject(
    val projectName: Int,
    val projectImageRes: Int
) {
    object CarbonAndroid: CarbonShellProject(
        projectName = R.string.main_app_title,
        projectImageRes = R.drawable.lumen_project
    )
    object Lumen: CarbonShellProject(
        projectName = R.string.lumen_title,
        projectImageRes = R.drawable.lumen_project
    )
}

val projects = listOf(CarbonShellProject.CarbonAndroid, CarbonShellProject.Lumen)

@OptIn(ExperimentalMaterialApi::class)
class CarbonShellAppState(val scaffoldState: BackdropScaffoldState) {
    val currentProject by mutableStateOf<CarbonShellProject>(CarbonShellProject.CarbonAndroid)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun rememberCarbonShellAppState(
    initialBackdropValue: BackdropValue = BackdropValue.Concealed,
    scaffoldState: BackdropScaffoldState = rememberBackdropScaffoldState(initialValue = initialBackdropValue)
) = remember {
    CarbonShellAppState(scaffoldState)
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CarbonShell(appState: CarbonShellAppState = rememberCarbonShellAppState()) {
    BackdropScaffold(
        appBar = { CarbonShellAppBar(appState) },
        backLayerContent = { CarbonShellBackLayerContent() },
        frontLayerContent = { CarbonShellFrontLayerContent() },
        scaffoldState = appState.scaffoldState,
        backLayerBackgroundColor = Neutron,
        backLayerContentColor = White100,
        frontLayerShape = RoundedCornerShape(0.dp)
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CarbonShellAppBar(appState: CarbonShellAppState) {
    TopAppBar(backgroundColor = Neutron, contentPadding = PaddingValues(horizontal = 16.dp)) {
        AnimatedVisibility(visible = appState.scaffoldState.currentValue == BackdropValue.Concealed) {
            Row(Modifier.fillMaxWidth()) {
                // Set the text's weight to consume the remaining horizontal space
                Text(
                    text = stringResource(id = R.string.lumen_title),
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    painter = painterResource(id = R.drawable.carbon_android_logo),
                    contentDescription = stringResource(id = R.string.cont_desc_shell),
                    modifier = Modifier.size(24.dp),
                    tint = Color.Unspecified
                )
            }
        }
    }
}

@Composable
fun CarbonShellBackLayerContent() {
    Box(modifier = Modifier.fillMaxSize()) {
        CarbonShellBackLayerLogo(
            modifier = Modifier
                .padding(horizontal = 100.dp, vertical = 116.dp)
                .align(Alignment.TopCenter)
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Bottom)
        ) {
            projects.forEach {
                ShellProjectItem(it) { }
            }
        }
    }
}

@Composable
fun CarbonShellBackLayerLogo(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy((-20).dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FadingCarbonShellLogo()
        Text(
            text = stringResource(id = R.string.carbon_shell_title),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CarbonShellFrontLayerContent() {}

@Composable
fun FadingCarbonShellLogo() {
    Image(
        painter = painterResource(id = R.drawable.carbon_android_logo),
        contentDescription = stringResource(id = R.string.cont_desc_shell),
        modifier = Modifier
            // Workaround to enable alpha compositing
            .graphicsLayer { alpha = 0.99f }
            .drawWithContent {
                drawContent()
                // Draw a Vertical gradient over the image fading from solid to translucent
                drawRect(
                    brush = Brush.verticalGradient(
                        listOf(
                            Color.Black,
                            Color.Transparent
                        )
                    ),
                    blendMode = BlendMode.DstIn
                )
            }
    )
}

@Preview
@Composable
fun ShellProjectItemPreview(
    @PreviewParameter(provider = ProjectItemParamPreview::class) projectItem: CarbonShellProject) {
    CarbonShellTheme {
        ShellProjectItem(projectItem)
    }
}


@Composable
fun ShellProjectItem(
    project: CarbonShellProject,
    onClick: () -> Unit = {}
) {
    Surface(
        modifier = Modifier
            .clickable { onClick() }
            .clip(MaterialTheme.shapes.medium)) {
        Column(modifier = Modifier
            .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)) {
            Image(
                painter = painterResource(id = project.projectImageRes),
                contentDescription = "",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2.160F)
                    .clip(carbonShapes.large)
            )
            Text(text = stringResource(id = project.projectName))
        }
    }
}

class ProjectItemParamPreview : PreviewParameterProvider<CarbonShellProject> {
    override val values: Sequence<CarbonShellProject>
        get() = projects.asSequence()
}
