package com.atomicrobot.carbon.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.atomicrobot.carbon.BuildConfig
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.navigation.CarbonScreens
import com.atomicrobot.carbon.navigation.LumenScreens
import com.atomicrobot.carbon.ui.lumen.navigation.LumenBottomSheetTask
import com.atomicrobot.carbon.ui.shader.AngledLinearGradient
import com.atomicrobot.carbon.ui.theme.DarkBlurple
import com.atomicrobot.carbon.ui.theme.LightBlurple
import com.atomicrobot.carbon.ui.theme.Neutron
import com.atomicrobot.carbon.ui.theme.ScreenHeading
import com.atomicrobot.carbon.ui.theme.White15
import com.atomicrobot.carbon.ui.theme.White25
import com.atomicrobot.carbon.ui.theme.White3
import com.atomicrobot.carbon.util.AppScreensPreviewProvider
import com.atomicrobot.carbon.util.LumenScreensPreviewProvider

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun TopBar(
    title: String = CarbonScreens.Home.title,
    buttonIcon: ImageVector = Icons.Filled.Menu,
    onButtonClicked: () -> Unit = {},
) = TopAppBar(
    colors = TopAppBarColors(
        containerColor = Neutron,
        titleContentColor = Color.White,
        scrolledContainerColor = Neutron,
        navigationIconContentColor = Color.White,
        actionIconContentColor = Color.White,
    ),
    title = {
        Row(
            modifier = Modifier.padding(end = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onButtonClicked() }) {
                Icon(imageVector = buttonIcon, contentDescription = "")
            }
            Text(text = title, modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(id = R.drawable.carbon_android_logo),
                contentDescription = stringResource(id = R.string.cont_desc_shell),
                modifier = Modifier.size(24.dp),
                tint = Color.Unspecified,
            )
        }
    }
)

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    buildVersion: String,
    fingerprint: String,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
                .padding(16.dp),
    ) {
        Text(
            text = stringResource(id = R.string.version_format, buildVersion),
            modifier = Modifier.weight(1f),
        )
        Text(
            text = stringResource(id = R.string.fingerprint_format, fingerprint),
        )
    }
}

@Preview
@Composable
fun BottomBarPreview() {
    BottomBar(
        buildVersion = BuildConfig.VERSION_NAME,
        fingerprint = BuildConfig.VERSION_FINGERPRINT,
    )
}

@Preview
@Composable
fun BottomNavigationBar(
    @PreviewParameter(
        AppScreensPreviewProvider::class,
        limit = 1,
    ) destinations: List<CarbonScreens>,
    navController: NavController = rememberNavController(),
    onDestinationClicked: (CarbonScreens) -> Unit = {},
) {
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        destinations.forEach { destination ->
            NavigationBarItem(
                selected =
                    currentDestination
                        ?.hierarchy
                        ?.any { it.route == destination.route } == true,
                icon = {
                    Icon(
                        destination.iconData.vectorData,
                        stringResource(id = destination.iconData.iconContentDescription),
                    )
                },
                onClick = { onDestinationClicked(destination) },
            )
        }
    }
}

@Composable
fun CustomSnackbar(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    SnackbarHost(
        modifier = modifier.fillMaxWidth(),
        hostState = hostState,
        snackbar = { snackbarData: SnackbarData ->
            CustomSnackBarContent(snackbarData.visuals.message)
        },
    )
}

@Composable
private fun CustomSnackBarContent(message: String) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(
                    color = SnackbarDefaults.color,
                )
                .padding(16.dp),
    ) {
        Text(
            text = message,
            color = SnackbarDefaults.actionColor,
        )
    }
}

@Preview
@Composable
fun CustomSnackBarContentPreview() {
    CustomSnackBarContent(message = "Snackbar sample content")
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun LumenTopAppBar(
    title: String = LumenScreens.Home.title,
    showAction: Boolean = true,
    bottomSheetTasks: List<LumenBottomSheetTask.LumenMenuTask> =
        listOf(
            LumenBottomSheetTask.AddScene,
        ),
    onTaskSelected: (LumenBottomSheetTask) -> Unit = {},
) = TopAppBar(
    title = {
        Text(
            text = title,
            color = Color.White,
            style = MaterialTheme.typography.ScreenHeading,
        )
    },
    actions = {
        if (showAction && bottomSheetTasks.isNotEmpty()) {
            var expanded by remember { mutableStateOf(false) }

            Box(
                modifier =
                    Modifier
                        .wrapContentSize(Alignment.TopEnd),
            ) {
                IconButton(
                    onClick = { expanded = true },
                    modifier =
                        Modifier
                            .size(48.dp)
                            .border(width = 2.dp, color = LightBlurple, shape = CircleShape)
                            .background(color = Color.White, shape = CircleShape),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_lumen_add),
                        contentDescription = stringResource(id = R.string.cont_desc_scene_add),
                        colorFilter = ColorFilter.tint(DarkBlurple),
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier =
                        Modifier
                            .width(450.dp)
                            .padding(32.dp),
                ) {
                    bottomSheetTasks.forEach {
                        DropdownMenuItem(
                            onClick = {
                                onTaskSelected(it)
                                expanded = false
                            },
                            text = {
                                Text(
                                    text = stringResource(id = it.titleRes),
                                    modifier = Modifier.padding(horizontal = 8.dp),
                                    style = MaterialTheme.typography.displayMedium,
                                )
                            }
                        )
                    }
                }
            }
        }
    },
    colors = TopAppBarDefaults.topAppBarColors(),
)

@Preview
@Composable
fun LumenBottomNavigationBar(
    @PreviewParameter(
        LumenScreensPreviewProvider::class,
        limit = 1,
    ) destinations: List<LumenScreens>,
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
    onDestinationClicked: (LumenScreens) -> Unit = {},
) {
    NavigationBar(modifier = modifier) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        destinations.forEach { destination ->

            val selected =
                currentDestination
                    ?.hierarchy
                    ?.any { it.route == destination.route } == true

            NavigationBarItem(
                selected = selected,
                icon = {
                    // Tweak the background if the navigation item is currently selected
                    val backgroundMod =
                        if (selected) {
                            Modifier
                                .clip(CircleShape)
                                .background(LightBlurple)
                                .border(
                                    width = 2.dp,
                                    brush =
                                        AngledLinearGradient(
                                            colors = listOf(White25, White15, White3),
                                            angleInDegrees = 0F,
                                            useAsCssAngle = false,
                                        ),
                                    CircleShape,
                                )
                        } else {
                            Modifier
                                .clip(CircleShape)
                                .background(Color.Transparent)
                        }
                    Image(
                        painterResource(id = destination.iconResourceId),
                        stringResource(id = destination.iconContentDescription),
                        modifier =
                            Modifier
                                .size(48.dp)
                                .then(backgroundMod)
                                .padding(
                                    horizontal = 13.67.dp,
                                    vertical = 14.dp,
                                ),
                    )
                },
                onClick = { onDestinationClicked(destination) },
            )
        }
    }
}
