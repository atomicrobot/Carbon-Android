package com.atomicrobot.carbon.ui.shell

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.navigation.CarbonScreens
import com.atomicrobot.carbon.ui.theme.CarbonShellTheme
import com.atomicrobot.carbon.ui.theme.Mono800
import com.atomicrobot.carbon.ui.theme.Neutron
import com.atomicrobot.carbon.ui.theme.White100
import com.atomicrobot.carbon.ui.theme.carbonShapes

sealed class CarbonShellProject(
    val projectName: Int,
    val projectImageRes: Int
) {
    object CarbonAndroid : CarbonShellProject(
        projectName = R.string.main_app_title,
        projectImageRes = R.drawable.carbon_android_logo
    )

    object Lumen : CarbonShellProject(
        projectName = R.string.lumen_title,
        projectImageRes = R.drawable.lumen_project
    )

    object Scanner : CarbonShellProject(
        projectName = R.string.scanner_title,
        projectImageRes = R.drawable.ic_baseline_qr_code_scanner
    )
}

val projects = listOf(CarbonShellProject.Lumen, CarbonShellProject.Scanner)
/*
val testProjects = listOf(
    CarbonShellProject.Lumen,
    CarbonShellProject.CarbonAndroid,
    CarbonShellProject.Lumen,
    CarbonShellProject.CarbonAndroid,
    CarbonShellProject.Lumen
)
*/

@Composable
fun CarbonShellNavigation(navController: NavController) {
    CarbonShellMainContent(navController)
}

@Composable
fun CarbonShellMainContent(navController: NavController) {
    Scaffold(
        modifier = Modifier.navigationBarsPadding(),
        scaffoldState = rememberScaffoldState(),
        topBar = {},
        bottomBar = {},
        backgroundColor = Color.Transparent
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .background(Neutron)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(vertical = 20.dp, horizontal = 16.dp)
            ) {

                item {
                    CarbonShellBackLayerLogo(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(vertical = 40.dp)
                    )
                }

                items(projects) {
                    ShellProjectItem(it) {
                        when (it.projectName) {
                            R.string.lumen_title -> {
                                navController.navigate(CarbonScreens.Lumen.route)
                            }
                            R.string.scanner_title -> {
                                navController.navigate(CarbonScreens.Scanner.route)
                            }
                        }
                    }
                }
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
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h4,
            color = White100
        )
    }
}

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
                        colors = listOf(
                            Color.Black,
                            Color.Transparent
                        ),
                        startY = 0.5f
                    ),
                    blendMode = BlendMode.DstIn
                )
            }
    )
}

@Composable
fun ShellProjectItem(
    project: CarbonShellProject,
    onClick: () -> Unit = {}
) {
    Surface(
        modifier = Modifier
            .clickable { onClick() }
//            .clip(MaterialTheme.shapes.medium),
            .clip(RoundedCornerShape(25.dp)),
        color = Mono800
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
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

@Preview
@Composable
fun CarbonShellMainContentPreview() {
    CarbonShellTheme {
        CarbonShellMainContent(navController = rememberNavController())
    }
}

@Preview
@Composable
fun CarbonShellBackLayerLogoPreview() {
    CarbonShellBackLayerLogo()
}

@Preview
@Composable
fun ShellProjectItemPreview(
    @PreviewParameter(provider = ProjectItemParamPreview::class) projectItem: CarbonShellProject
) {
    CarbonShellTheme {
        ShellProjectItem(projectItem)
    }
}
