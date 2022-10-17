package com.atomicrobot.carbon.ui.lumen.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.navigation.LumenScreens
import com.atomicrobot.carbon.navigation.LumenTopAppBar
import com.atomicrobot.carbon.navigation.lumenScreens
import com.atomicrobot.carbon.ui.components.LumenBottomNavigationBar
import com.atomicrobot.carbon.ui.lumen.home.LumenHomeScreen
import com.atomicrobot.carbon.ui.lumen.routines.LumenRoutinesScreen
import com.atomicrobot.carbon.ui.lumen.scenes.AddSceneTask
import com.atomicrobot.carbon.ui.lumen.scenes.EditSceneTask
import com.atomicrobot.carbon.ui.lumen.scenes.ScenesScreen
import com.atomicrobot.carbon.ui.lumen.settings.LumenSettingsScreen
import com.atomicrobot.carbon.ui.shell.CarbonShellNestedAppBar
import com.atomicrobot.carbon.ui.theme.LumenTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

sealed class LumenBottomSheetTask {
    // I can't think of a better way to blank the task sheet
    object NoTask : LumenBottomSheetTask()
    abstract class LumenMenuTask(val titleRes: Int) : LumenBottomSheetTask()
    object AddScene : LumenMenuTask(R.string.add_scene)
    data class EditScene(val sceneId: Long) : LumenBottomSheetTask()
}

val bottomSheetTasks: List<LumenBottomSheetTask.LumenMenuTask> = listOf(
    LumenBottomSheetTask.AddScene,
)

@OptIn(ExperimentalMaterialApi::class)
class LumenAppState(
    val modalBottomSheetState: ModalBottomSheetState,
    val navController: NavHostController,
    val scaffoldState: ScaffoldState,
    initialBottomSheetTask: LumenBottomSheetTask
) {
    var currentBottomSheetTask by mutableStateOf<LumenBottomSheetTask>(initialBottomSheetTask)
        private set

    suspend fun showBottomSheetForTask(bottomSheetTask: LumenBottomSheetTask) {
        currentBottomSheetTask = bottomSheetTask
        modalBottomSheetState.show()
    }

    suspend fun clearBottomSheetTask() {
        modalBottomSheetState.hide()
        currentBottomSheetTask = LumenBottomSheetTask.NoTask
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun rememberLumenAppState(
    modalBottomSheetState: ModalBottomSheetState = rememberModalBottomSheetState(
        ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    ),
    navController: NavHostController = rememberNavController(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    bottomSheetTask: LumenBottomSheetTask = remember { LumenBottomSheetTask.NoTask }
) = remember(modalBottomSheetState, navController, scaffoldState, bottomSheetTask) {
    LumenAppState(modalBottomSheetState, navController, scaffoldState, bottomSheetTask)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DesignLumenNavigation(appState: LumenAppState = rememberLumenAppState()) {
    LaunchedEffect(appState.modalBottomSheetState.currentValue) {
        if (appState.modalBottomSheetState.currentValue == ModalBottomSheetValue.Hidden) {
            appState.clearBottomSheetTask()
        }
    }

    ModalBottomSheetLayout(
        sheetContent = { LumenBottomSheet(appState) },
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize(),
        sheetState = appState.modalBottomSheetState,
        scrimColor = Color.Transparent
    ) {
        LumenMainContent(appState)
    }
}

@Composable
fun appBarTitle(navBackStackEntry: NavBackStackEntry?): String {
    return when (navBackStackEntry?.destination?.route) {
        LumenScreens.Home.route -> LumenScreens.Home.title
        LumenScreens.Routines.route -> LumenScreens.Settings.title
        LumenScreens.Scenes.route -> LumenScreens.Scenes.title
        LumenScreens.Settings.route -> LumenScreens.Settings.title
        else -> ""
    }
}

@Composable
fun LumenMainContent(appState: LumenAppState) {
    val navBackStackEntry: NavBackStackEntry? by appState
        .navController
        .currentBackStackEntryAsState()

    val showAppBarAction = navBackStackEntry?.destination?.route != LumenScreens.Settings.route
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    Column {
        CarbonShellNestedAppBar(stringResource(id = R.string.lumen_title))
        Scaffold(
            modifier = Modifier.navigationBarsPadding(),
            scaffoldState = appState.scaffoldState,
            topBar = {
                LumenTopAppBar(
                    title = appBarTitle(navBackStackEntry = navBackStackEntry),
                    showAction = showAppBarAction,
                    bottomSheetTasks = bottomSheetTasks
                ) {
                    coroutineScope.launch {
                        appState.showBottomSheetForTask(it)
                    }
                }
            },
            bottomBar =
            {
                LumenBottomNavigationBar(
                    modifier = Modifier.height(88.dp),
                    destinations = lumenScreens,
                    navController = appState.navController,
                ) {
                    if (appState.navController.currentBackStackEntry?.destination?.route != it.route) {
                        appState.navController.navigate(it.route) {
                            // Make sure the back stack only consists of the current graphs main
                            // destination
                            popUpTo(LumenScreens.Scenes.route) {
                                saveState = true
                            }
                            // Singular instance of destinations
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            },
            backgroundColor = Color.Transparent
        ) { innerPadding ->
            NavHost(
                modifier = Modifier.padding(innerPadding),
                navController = appState.navController,
                startDestination = "Main"
            ) {
                mainLumenGraph(appState)
            }
        }
    }
}

fun NavGraphBuilder.mainLumenGraph(appState: LumenAppState) {
    navigation(startDestination = LumenScreens.Scenes.route, route = "Main") {
        composable(LumenScreens.Home.route) { LumenHomeScreen() }
        composable(LumenScreens.Routines.route) { LumenRoutinesScreen() }
        composable(LumenScreens.Scenes.route) {
            val coroutineScope: CoroutineScope = rememberCoroutineScope()
            ScenesScreen {
                coroutineScope.launch {
                    appState.showBottomSheetForTask(LumenBottomSheetTask.EditScene(it))
                }
            }
        }
        composable(LumenScreens.Settings.route) { LumenSettingsScreen() }
    }
}

@Composable
fun LumenBottomSheet(appState: LumenAppState) {
    val bottomSheetTask = appState.currentBottomSheetTask
    val coroutineScope: CoroutineScope = rememberCoroutineScope()

    val dismissBottomSheet: () -> Unit = {
        coroutineScope.launch { appState.clearBottomSheetTask() }
    }

    Box(
        Modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {
        when (bottomSheetTask) {
            LumenBottomSheetTask.AddScene -> AddSceneTask(onDismissed = dismissBottomSheet)
            is LumenBottomSheetTask.EditScene -> EditSceneTask(
                bottomSheetTask.sceneId,
                onDismissed = dismissBottomSheet
            )
            LumenBottomSheetTask.NoTask -> { /* INTENTIONALLY LEFT BLANK */
            }
            else -> { /* INTENTIONALLY LEFT BLANK */
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DesignLumenNavigationPreview() {
    LumenTheme {
        DesignLumenNavigation()
    }
}
