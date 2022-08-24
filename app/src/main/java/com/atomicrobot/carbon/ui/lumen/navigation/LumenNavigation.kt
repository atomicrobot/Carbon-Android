import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
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
import com.atomicrobot.carbon.ui.compose.ScenePreviewProvider
import com.atomicrobot.carbon.ui.lumen.scenes.ScenesScreen
import com.atomicrobot.carbon.ui.shader.AngledLinearGradient
import com.atomicrobot.carbon.ui.theme.CardBackgroundOn
import com.atomicrobot.carbon.ui.theme.RoundedTextField
import com.atomicrobot.carbon.ui.theme.White100
import com.atomicrobot.carbon.ui.theme.White3
import com.atomicrobot.carbon.ui.theme.White50
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

sealed class LumenBottomSheetTask {
    object AddScene: LumenBottomSheetTask()
    data class EditScene(val scene: Scene): LumenBottomSheetTask()
}

@OptIn(ExperimentalMaterialApi::class)
class LumenAppState(
    val modalBottomSheetState: ModalBottomSheetState,
    val navController: NavHostController,
    val scaffoldState: ScaffoldState,
    initialBottomSheetTask: LumenBottomSheetTask) {

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
                skipHalfExpanded = true),
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
fun LumenMainContent(appState: LumenAppState) {
    val navBackStackEntry: NavBackStackEntry? by appState
            .navController
            .currentBackStackEntryAsState()

    val showAppBarAction = navBackStackEntry?.destination?.route != LumenScreens.Settings.route
    Scaffold(
        scaffoldState = appState.scaffoldState,
        topBar = {
            LumenTopAppBar(
                title = appBarTitle(navBackStackEntry = navBackStackEntry),
                showAction = showAppBarAction
            ) {

            }
        },
        bottomBar =
        {
            LumenBottomNavigationBar(
                modifier = Modifier.height(88.dp),
                destinations = lumenScreens,
                navController = appState.navController,
            ){
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
        backgroundColor = Color.Transparent) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = appState.navController,
            startDestination = "Main") {
            mainLumenGraph(appState)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
fun NavGraphBuilder.mainLumenGraph(appState: LumenAppState) {
    navigation(startDestination = LumenScreens.Scenes.route, route = "Main") {
        composable(LumenScreens.Home.route) { HomeScreen() }
        composable(LumenScreens.Routines.route) { RoutineScreen() }
        composable(LumenScreens.Scenes.route) {
            val coroutineScope: CoroutineScope = rememberCoroutineScope()
            ScenesScreen {
                coroutineScope.launch {
                    appState.showBottomSheetForTask(LumenBottomSheetTask.EditScene(it))
                }
            }
        }
        composable(LumenScreens.Settings.route) { SettingsScreen() }
    }
}

@Composable
fun LumenMenu() {}

@Composable
fun HomeScreen() {}

@Composable
fun RoutineScreen() {}

@Composable
fun SettingsScreen() {}

@Composable
fun AddSceneTask(onTaskComplete: () -> Unit = {}) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.size(48.dp))
            Text(
                text = "Grow Lights",
                modifier = Modifier.weight(1F),
                color = White100,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h2)
            Image(
                painter = painterResource(id = R.drawable.ic_lumen_add),
                modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .clickable {  }
                        .rotate(45F),
                contentDescription = stringResource(id = R.string.cont_desc_menu_close)
            )
        }
    }
}

@Preview
@Composable
fun EditSceneTask(
        @PreviewParameter(ScenePreviewProvider::class, limit = 1) scene: Scene,
        onTaskComplete: () -> Unit = {}) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.ic_lumen_trash),
                modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .clickable {  }
                        .padding(10.dp),
                contentDescription = stringResource(id = R.string.cont_desc_scene_remove)
            )
            Text(
                text = scene.name,
                modifier = Modifier.weight(1F),
                color = White100,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h2)
            Image(
                painter = painterResource(id = R.drawable.ic_lumen_add),
                modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .clickable { onTaskComplete() }
                        .rotate(45F),
                contentDescription = stringResource(id = R.string.cont_desc_menu_close)
            )
        }

        Text(
            text = "Name",
            modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .padding(vertical = 8.dp),
            color = White100,
            style = MaterialTheme.typography.body2
        )
        TextField(
                value = scene.name,
                onValueChange = {},
                modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .border(
                                width = 1.dp,
                                brush = AngledLinearGradient(
                                        colors = listOf(White50, White3),
                                        angleInDegrees = -135F,
                                        useAsCssAngle = true),
                                shape = RoundedCornerShape(8.dp))
                        .background(
                                color = CardBackgroundOn,
                                shape = RoundedCornerShape(8.dp)
                        ),
                placeholder = {
                    Text(
                        text = "Name your Scene",
                        color = White50,
                        style = MaterialTheme.typography.body1)
                },
                singleLine = true,
                shape = MaterialTheme.shapes.RoundedTextField,
                colors = TextFieldDefaults.textFieldColors(
                        textColor = White100,
                        cursorColor = White100,
                        disabledTextColor = Color.Transparent,
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                ))

        Text(
            text = scene.room.name,
            modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .padding(top = 16.dp, bottom = 8.dp),
                color = White100,
            style = MaterialTheme.typography.body2
        )
        TextField(
            value = "",
            onValueChange = {},
            modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .border(
                            width = 1.dp,
                            brush = AngledLinearGradient(
                                    colors = listOf(White50, White3),
                                    angleInDegrees = -135F,
                                    useAsCssAngle = true),
                            shape = RoundedCornerShape(8.dp)
                    )
                    .background(
                            color = CardBackgroundOn,
                            shape = RoundedCornerShape(8.dp)
                    ),
            placeholder = {
                Text(
                    text = "Select a room",
                    color = White50,
                    style = MaterialTheme.typography.body1
                )
            },
            singleLine = true,
            shape = MaterialTheme.shapes.RoundedTextField,
            colors = TextFieldDefaults.textFieldColors(
                    textColor = White100,
                    cursorColor = White100,
                    disabledTextColor = Color.Transparent,
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
            )
        )

        Text(
            text = "Duration",
            modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .padding(top = 16.dp, bottom = 8.dp),
            color = White100,
            style = MaterialTheme.typography.body2
        )
        TextField(
            value = scene.duration,
            onValueChange = {},
            modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .border(
                            width = 1.dp,
                            brush = AngledLinearGradient(
                                    colors = listOf(White50, White3),
                                    angleInDegrees = -135F,
                                    useAsCssAngle = true),
                            shape = RoundedCornerShape(8.dp)
                    )
                    .background(
                            color = CardBackgroundOn,
                            shape = RoundedCornerShape(8.dp)
                    ),
            placeholder = {
                Text(
                    text = "Select a duration",
                    color = White50,
                    style = MaterialTheme.typography.body1
                )
            },
            singleLine = true,
            shape = MaterialTheme.shapes.RoundedTextField,
            colors = TextFieldDefaults.textFieldColors(
                    textColor = White100,
                    cursorColor = White100,
                    disabledTextColor = Color.Transparent,
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
            )
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LumenBottomSheet(appState: LumenAppState) {
    val bottomSheetTask = appState.currentBottomSheetTask
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    when(bottomSheetTask) {
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