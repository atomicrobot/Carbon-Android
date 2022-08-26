import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.atomicrobot.carbon.data.lumen.Scene
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

sealed class LumenBottomSheetTask(val titleRes: Int) {

    object AddDevice : LumenBottomSheetTask(R.string.add_device)
    object AddScene : LumenBottomSheetTask(R.string.add_scene)
    object AddRoutine : LumenBottomSheetTask(R.string.add_routine)
    object AddWidget : LumenBottomSheetTask(R.string.add_widget)
    data class EditScene(val scene: Scene) : LumenBottomSheetTask(R.string.edit_scene)
}

val bottomSheetTasks = listOf(
    LumenBottomSheetTask.AddDevice,
    LumenBottomSheetTask.AddScene,
    LumenBottomSheetTask.AddRoutine,
    LumenBottomSheetTask.AddWidget
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
    bottomSheetTask: LumenBottomSheetTask = remember { LumenBottomSheetTask.AddScene }
) = remember(modalBottomSheetState, navController, scaffoldState, bottomSheetTask) {
    LumenAppState(modalBottomSheetState, navController, scaffoldState, bottomSheetTask)
}

@OptIn(ExperimentalMaterialApi::class)
@Preview()
@Composable
fun DesignLumenNavigation(appState: LumenAppState = rememberLumenAppState()) {
    ModalBottomSheetLayout(
        sheetContent = { LumenBottomSheet(appState) },
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize(),
        sheetState = appState.modalBottomSheetState
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
    Scaffold(
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LumenBottomSheet(appState: LumenAppState) {
    val bottomSheetTask = appState.currentBottomSheetTask
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    when (bottomSheetTask) {
        LumenBottomSheetTask.AddScene ->
            AddSceneTask()
        is LumenBottomSheetTask.EditScene ->
            EditSceneTask(bottomSheetTask.scene) {
                coroutineScope.launch {
                    appState.modalBottomSheetState.hide()
                }
            }
        else -> { /* INTENTIONALLY LEFT BLANK */ }
    }
}
