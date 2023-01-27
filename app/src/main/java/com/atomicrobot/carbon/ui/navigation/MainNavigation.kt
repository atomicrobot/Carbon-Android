package com.atomicrobot.carbon.ui.navigation

import android.graphics.Color
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.*
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.atomicrobot.carbon.navigation.CarbonScreens
import com.atomicrobot.carbon.navigation.CarbonScreens.DeepLink.arguments
import com.atomicrobot.carbon.navigation.CarbonScreens.DeepLink.title
import com.atomicrobot.carbon.navigation.appScreens
import com.atomicrobot.carbon.navigation.drawerScreens
import com.atomicrobot.carbon.ui.about.AboutHtmlScreen
import com.atomicrobot.carbon.ui.about.AboutScreen
import com.atomicrobot.carbon.ui.components.*
import com.atomicrobot.carbon.ui.deeplink.DeepLinkSampleScreen
import com.atomicrobot.carbon.ui.designSystems.DesignSystemDetailScreen
import com.atomicrobot.carbon.ui.designSystems.DesignSystemScreen
import com.atomicrobot.carbon.ui.designSystems.DesignSystemViewModel
import com.atomicrobot.carbon.ui.designSystems.DesignSystemViewModel.Event
import com.atomicrobot.carbon.ui.license.LicenseScreen
import com.atomicrobot.carbon.ui.main.MainScreen
import com.atomicrobot.carbon.ui.settings.SettingsScreen
import com.atomicrobot.carbon.ui.theme.CarbonAndroidTheme
import com.atomicrobot.carbon.util.LocalActivity
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun CarbonAndroidApp() {
    CarbonAndroidTheme {
        val parentActivity = LocalContext.current as ComponentActivity
        // Wrap the composable in a LocalActivity provider so our composable 'environment'
        // has access to Activity context/scope which is required for requesting permissions
        CompositionLocalProvider(LocalActivity provides parentActivity) {
            CarbonAndroid()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun CarbonAndroid() {
    val navController = rememberAnimatedNavController()
    val snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }

    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val navigationActions: NavigationActions = remember {
        NavigationActions(navController, scope, drawerState)
    }

    val appBarState by navigationActions.appBarState.collectAsState()

    BackHandler(enabled = true) {
        if (drawerState.isOpen){
            navigationActions.toggleDrawer()
        } else {
            navigationActions.back()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                content = {
                    Drawer(
                        screens = drawerScreens,
                        onDestinationClicked = { screen ->
                            navigationActions.toggleDrawer()
                            navigationActions.navigateToScreen(screen.route)
                        }
                    )
                }
            )
        },
        content = {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(text = appBarState.title) },
                        navigationIcon = { appBarState.navigation?.invoke() },
                        actions = { appBarState.actions?.invoke(this) }
                    )
                },
                bottomBar = {
                    BottomNavigationBar(
                        destinations = appScreens,
                        navController = navController,
                        onDestinationClicked = { screen ->
                            navigationActions.navigateToScreen(screen.route)
                        }
                    )
                },
                snackbarHost = { SnackbarHost(snackbarHostState) }
            ) { innerPadding ->
                AnimatedNavHost(
                    modifier = Modifier.padding(innerPadding),
                    navController = navController,
                    startDestination = CarbonScreens.Home.route
                ){
                    carbonNavGraph(
                        navigationActions = navigationActions,
                        snackbarHostState = snackbarHostState,
                    )
                }
            }
        }
    )
}

/**
 * Nested nav. graph dedicated to displaying 'main' app content.
 */
fun NavGraphBuilder.carbonNavGraph(
    navigationActions: NavigationActions,
    snackbarHostState: SnackbarHostState,
) {

    carbonFlow(
        snackbarHostState = snackbarHostState,
        onUpdateAppBarState = navigationActions::updateAppBar,
        onDrawerClicked = navigationActions::toggleDrawer
    )

    designSystemFlow(
        onUpdateAppBarState = navigationActions::updateAppBar,
        onDrawerClicked = navigationActions::toggleDrawer,
        onDetailSelected = navigationActions::navigateToScreen,
        onDetailBack = navigationActions::back
    )
}

@OptIn(ExperimentalMaterial3Api::class)
class NavigationActions(
    val navController: NavHostController,
    val scope: CoroutineScope,
    val drawerState: DrawerState
){

    private val _appBarState = MutableStateFlow(
        AppBarState.defaultAppBarState(
            title = CarbonScreens.Home.title,
            onDrawerClicked = { toggleDrawer() }
        )
    )

    val appBarState: StateFlow<AppBarState> = _appBarState.asStateFlow()

    val currentNavDestination: NavDestination?
    get() = navController.currentBackStackEntry?.destination

    fun toggleDrawer() {
        scope.launch {
            if (drawerState.isOpen){
                drawerState.close()
            } else {
                drawerState.open()
            }
        }
    }

    fun navigateToScreen(route: String) {
        if (currentNavDestination?.route != route){
            navController.navigate(route){
                launchSingleTop = true

                Timber.d("PPPP, ${currentNavDestination?.route}")

                if (currentNavDestination?.route != CarbonScreens.DesignSystems.route){
                    popUpTo(CarbonScreens.Home.route){
                        saveState = true
                    }
                    restoreState = true
                }
            }
        }
    }

    fun back(){
        navController.navigateUp()
    }

    fun updateAppBar(
        appBarState: AppBarState
    ){
        _appBarState.update {
            appBarState
        }
    }

}

data class AppBarState(
    val title: String = "",
    val navigation: (@Composable () -> Unit)? = null,
    val actions: (@Composable RowScope.() -> Unit)? = null
) {

    companion object {
        fun defaultAppBarState(
            title: String,
            onDrawerClicked: () -> Unit
        ): AppBarState {
            return AppBarState(
                title = title,
                navigation = {
                     CarbonTopBarNavigation(
                         onDrawerClicked = onDrawerClicked
                     )
                },
                actions = {
                    CarbonTopBarActions()
                }
            )
        }
    }
}