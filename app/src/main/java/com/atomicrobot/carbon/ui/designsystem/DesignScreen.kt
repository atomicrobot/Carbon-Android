package com.atomicrobot.carbon.ui.designsystem

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.atomicrobot.carbon.navigation.CarbonScreens
import com.atomicrobot.carbon.ui.designsystem.theme.DesignRadiosScreen
import com.atomicrobot.carbon.ui.theme.CarbonAndroidTheme
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import org.koin.androidx.compose.getViewModel

//region Composables
@Composable
fun appBarTitle(navBackStackEntry: NavBackStackEntry?): String {
    navBackStackEntry?.destination?.route?.let { currentRoute ->
        val destination = CarbonScreens.values().find { currentRoute == it.route }
        return destination?.title ?: ""
    }
    return ""
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun DesignScreen(
    onBackClicked: () -> Unit,
) {
    val viewModel: DesignSystemViewModel = getViewModel()
    val screenState: DesignSystemViewModel.ScreenState by viewModel.uiState.collectAsState()
    val navController: NavHostController = rememberAnimatedNavController()

    CarbonAndroidTheme(
        darkTheme = screenState.darkMode,
        fontScale = screenState.fontScale.scale,
    ) {
        Scaffold(
            topBar = {
                val navBackStackEntry: NavBackStackEntry? by navController.currentBackStackEntryAsState()
                DesignScreenAppBar(
                    title = appBarTitle(navBackStackEntry = navBackStackEntry),
                    screenState.darkMode,
                    selectedFontScale = screenState.fontScale,
                    onBackPressed = onBackClicked,
                    onFontScaleChanged = {
                        viewModel.updateFontScale(it)
                    },
                    onDarkModeChanged = {
                        viewModel.enabledDarkMode(it)
                    }
                )
            },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            AnimatedNavHost(
                modifier = Modifier.padding(innerPadding),
                navController = navController,
                startDestination = "design_system"
            ) {
                designSystemGraph {
                    navController.navigate(it)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DesignScreenAppBar(
    title: String,
    darkModeEnabled: Boolean = false,
    selectedFontScale: FontScale = FontScale.Normal,
    onBackPressed: () -> Unit = {},
    onFontScaleChanged: (FontScale) -> Unit = {},
    onDarkModeChanged: (Boolean) -> Unit = {},
) {
    var expanded by remember { mutableStateOf(false) }
    TopAppBar(
        title = {
            Text(text = title)
        },
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(Icons.Outlined.ArrowBack, contentDescription = "Navigate back")
            }
        },
        actions = {
            ExposedDropdownMenuBox(
                expanded = expanded,
                modifier = Modifier.size(200.dp, 56.dp),
                onExpandedChange = { expanded = it }
            ) {
                TextField(
                    // The `menuAnchor` modifier must be passed to the text field for correctness.
                    modifier = Modifier.menuAnchor(),
                    readOnly = true,
                    value = selectedFontScale.label(LocalContext.current),
                    onValueChange = {},
                    trailingIcon = { TrailingIcon(expanded = expanded) },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    fontScales.forEach {
                        DropdownMenuItem(
                            text = { Text(it.label(LocalContext.current)) },
                            onClick = {
                                onFontScaleChanged(it)
                                expanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        )
                    }
                }
            }

            IconToggleButton(
                checked = darkModeEnabled,
                onCheckedChange = onDarkModeChanged
            ) {
                Icon(
                    imageVector = if (darkModeEnabled) Icons.Filled.DarkMode
                    else Icons.Filled.LightMode,
                    if (darkModeEnabled) "Enable Light mode"
                    else "Enable Dark mode"
                )
            }
        }
    )
}

//region NavGraphBuilder extension
@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.designSystemGraph(
    onNavigateTo: (String) -> Unit = {}
) {
    val onEnterTransition: AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition? = {
        when (targetState.destination.route) {
            DesignSystemScreens.Home.route ->
                slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(500))
            else -> slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(500))
        }
    }
    val onExitTransition: AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition? = {
        when (initialState.destination.route) {
            DesignSystemScreens.Home.route -> {
                slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(500))
            }
            else -> slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(500))
        }
    }
    navigation(
        startDestination = DesignSystemScreens.Home.route,
        route = "design_system",
        enterTransition = onEnterTransition,
        exitTransition = onExitTransition,
    ) {
        composable(route = DesignSystemScreens.Home.route) {
            DesignScreenHome(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 2.dp),
                onNavigateTo
            )
        }
        composable(route = DesignSystemScreens.Colors.route) {
            DesignColorsScreen(
                modifier = Modifier
                    .fillMaxSize(),
            )
        }
        composable(route = DesignSystemScreens.Typography.route) {
            DesignTypographyScreen(
                modifier = Modifier
                    .fillMaxSize(),
            )
        }
        composable(route = DesignSystemScreens.Buttons.route) {
            DesignButtonsScreen(
                modifier = Modifier
                    .fillMaxSize(),
            )
        }
        composable(route = DesignSystemScreens.Checkboxes.route) {
            DesignCheckboxesScreen(
                modifier = Modifier
                    .fillMaxSize(),
            )
        }
        composable(route = DesignSystemScreens.Radios.route) {
            DesignRadiosScreen(
                modifier = Modifier
                    .fillMaxSize(),
            )
        }
        composable(route = DesignSystemScreens.Sliders.route) {
            DesignSlidersScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 2.dp),
            )
        }
        composable(route = DesignSystemScreens.Switches.route) {
            DesignSwitchesScreen(
                modifier = Modifier
                    .fillMaxSize(),
            )
        }
    }
}
//endregion
//endregion

@Preview
@Composable
fun DesignScreenAppBarPreview() {
    DesignScreenAppBar(title = "Carbon Android")
}
