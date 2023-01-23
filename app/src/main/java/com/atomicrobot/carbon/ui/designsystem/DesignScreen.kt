package com.atomicrobot.carbon.ui.designsystem

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.atomicrobot.carbon.ui.designsystem.theme.DesignRadiosScreen
import com.atomicrobot.carbon.ui.theme.CarbonAndroidTheme
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import org.koin.androidx.compose.getViewModel

val textScalingOptions = listOf("0.75 Text Scale", "1.0 Text Scale", "1.5 Text Scale", "2.0 Text Scale",)

//region Composables
@Composable
fun appBarTitle(navBackStackEntry: NavBackStackEntry?): String {
    return when (navBackStackEntry?.destination?.route) {
        DesignSystemScreens.Home.route -> stringResource(id = DesignSystemScreens.Home.title)
        DesignSystemScreens.Colors.route -> stringResource(id = DesignSystemScreens.Colors.title)
        DesignSystemScreens.Typography.route -> stringResource(id = DesignSystemScreens.Typography.title)
        DesignSystemScreens.Buttons.route -> stringResource(id = DesignSystemScreens.Buttons.title)
        DesignSystemScreens.Checkboxes.route -> stringResource(id = DesignSystemScreens.Buttons.title)
        DesignSystemScreens.Radios.route -> stringResource(id = DesignSystemScreens.Buttons.title)
        DesignSystemScreens.Sliders.route -> stringResource(id = DesignSystemScreens.Switches.title)
        DesignSystemScreens.Switches.route -> stringResource(id = DesignSystemScreens.Switches.title)
        else -> ""
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DesignScreenAppBar(
    navBackStackEntry: NavBackStackEntry?,
    darkModeEnabled: Boolean,
    onBackPressed:() -> Unit = {},
    onDarkModeChanged: (Boolean) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(textScalingOptions[1]) }
    TopAppBar(
        title = {
            Text(text = appBarTitle(navBackStackEntry))
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
                onExpandedChange = { expanded = it}) {
                TextField(
                    // The `menuAnchor` modifier must be passed to the text field for correctness.
                    modifier = Modifier.menuAnchor(),
                    readOnly = true,
                    value = selectedOptionText,
                    onValueChange = {},
                    trailingIcon = { TrailingIcon(expanded = expanded) },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    textScalingOptions.forEach {
                        DropdownMenuItem(
                            text = { Text(it) },
                            onClick = {
                                selectedOptionText = it
                                expanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        )
                    }
                }
            }

            IconToggleButton(
                checked = darkModeEnabled,
                onCheckedChange = onDarkModeChanged) {
                Icon(
                   imageVector = if(darkModeEnabled) Icons.Filled.DarkMode
                   else Icons.Filled.LightMode,
                    if(darkModeEnabled) "Enable Light mode"
                    else "Enable Dark mode"
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun DesignScreen() {
    val viewModel: DesignSystemViewModel = getViewModel()
    val screenState: DesignSystemViewModel.ScreenState by viewModel.uiState.collectAsState()
    val navController: NavHostController = rememberAnimatedNavController()
    val onBackPressedDispatcherOwner = LocalOnBackPressedDispatcherOwner.current

    CarbonAndroidTheme(darkTheme = screenState.darkMode) {
        Scaffold(
            topBar = {
                val navBackStackEntry: NavBackStackEntry? by navController.currentBackStackEntryAsState()
                DesignScreenAppBar(
                    navBackStackEntry,
                    screenState.darkMode,
                    onBackPressed = {
                        onBackPressedDispatcherOwner?.onBackPressedDispatcher?.onBackPressed()
                    }
                ) {
                    viewModel.enabledDarkMode(it)
                }
            },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            AnimatedNavHost(
                modifier = Modifier.padding(innerPadding),
                navController = navController,
                startDestination = "design"
            ) {
                designSystemGraphFlow {
                    navController.navigate(it)
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.designSystemGraphFlow(onNavigateTo: (String) -> Unit = {}) {
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
        route = "design",
        enterTransition = onEnterTransition,
        exitTransition = onExitTransition,
    ) {
        composable(route = DesignSystemScreens.Home.route) {
            DesignScreenHome(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 2.dp),
                onNavigateTo)
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

@Preview
@Composable
fun DesignScreenAppBarPreview() {
    DesignScreenAppBar(null, false) {

    }
}