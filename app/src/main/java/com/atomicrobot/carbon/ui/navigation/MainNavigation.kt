package com.atomicrobot.carbon.ui.navigation

import android.graphics.Color
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumedWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navOptions
import com.atomicrobot.carbon.navigation.CarbonScreens
import com.atomicrobot.carbon.navigation.CarbonScreens.DeepLink.getTextColor
import com.atomicrobot.carbon.navigation.CarbonScreens.DeepLink.getTextSize
import com.atomicrobot.carbon.navigation.appScreens
import com.atomicrobot.carbon.navigation.drawerScreens
import com.atomicrobot.carbon.ui.about.AboutHtmlScreen
import com.atomicrobot.carbon.ui.about.AboutScreen
import com.atomicrobot.carbon.ui.components.BottomNavigationBar
import com.atomicrobot.carbon.ui.components.CustomSnackbar
import com.atomicrobot.carbon.ui.deeplink.DeepLinkSampleScreen
import com.atomicrobot.carbon.ui.designsystem.designSystemGraph
import com.atomicrobot.carbon.ui.license.LicenseScreen
import com.atomicrobot.carbon.ui.main.MainScreen
import com.atomicrobot.carbon.ui.settings.SettingsScreen
import com.atomicrobot.carbon.ui.theme.CarbonAndroidTheme
import com.atomicrobot.carbon.util.LocalActivity
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

//region Carbon Composables
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CarbonAndroid(
    appState: CarbonAndroidAppState = rememberCarbonAndroidAppState(),
) {
    // Intercept back events when the nav. drawer is opened
    BackHandler(enabled = appState.drawerState.isOpen, onBack = appState::onBack)

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Drawer(screens = drawerScreens) { screen ->
                    appState.navigateToCarbonScreen(screen)
                }
            }
        },
        drawerState = appState.drawerState,
    ) {
        Scaffold(
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            bottomBar = {
                AnimatedVisibility(
                    visible = appState.shouldShowBottomBar,
                    enter = slideInVertically { it } + fadeIn(),
                    exit = slideOutVertically { it } + fadeOut(),
                ) {
                    BottomNavigationBar(
                        destinations = appScreens,
                        currentDestination = appState.currentDestination,
                        onDestinationClicked = appState::navigateToCarbonScreen,
                    )
                }
            },
            snackbarHost = { CustomSnackbar(appState.snackbarHostState) },
        ) { innerPadding ->
            CarbonAndroidNavHost(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .consumedWindowInsets(innerPadding)
                    .windowInsetsPadding(
                        WindowInsets.safeDrawing.only(
                            WindowInsetsSides.Horizontal
                        )
                    ),
                navController = appState.navController,
                snackbarHostState = appState.snackbarHostState,
                onNavigateTo = appState::navigateToRoute,
                onNavIconClicked = appState::onNavIconClicked
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CarbonAndroidNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    onNavigateTo: (String) -> Unit,
    onNavIconClicked: () -> Unit,
) {
    AnimatedNavHost(
        modifier = modifier,
        navController = navController,
        startDestination = "Main",
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        carbonNavGraph(
            snackbarHostState,
            onNavIconClicked
        )
        designSystemGraph(
            navController = navController,
            onNavigateTo = onNavigateTo,
            onNavIconClicked
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.carbonNavGraph(
    snackbarHostState: SnackbarHostState,
    onNavIconClicked: () -> Unit,
) {
    navigation(
        startDestination = CarbonScreens.Home.route,
        route = "Main"
    ) {
        composable(CarbonScreens.Home.route) {
            MainScreen(
                snackbarHostState = snackbarHostState,
                onNavIconClicked = onNavIconClicked
            )
        }
        composable(CarbonScreens.Settings.route) {
            SettingsScreen(
                onNavIconClicked = onNavIconClicked
            )
        }
        composable(
            route = CarbonScreens.DeepLink.routeWithArgs,
            arguments = CarbonScreens.DeepLink.arguments,
            deepLinks = CarbonScreens.DeepLink.deepLink,
        ) {
            val textColor = remember {
                it.getTextColor(Color.BLACK)
            }
            val textSize = remember {
                it.getTextSize(30.0F)
            }
            DeepLinkSampleScreen(
                textColor = textColor,
                textSize = textSize,
                onNavIconClicked = onNavIconClicked
            )
        }
        composable(CarbonScreens.About.route) {
            AboutScreen(onNavIconClicked = onNavIconClicked)
        }
        composable(CarbonScreens.AboutHtml.route) {
            AboutHtmlScreen(onNavIconClicked = onNavIconClicked)
        }
        composable(CarbonScreens.License.route) {
            LicenseScreen(
                snackbarHostState = snackbarHostState,
                onNavIconClicked = onNavIconClicked
            )
        }
    }
}

//region Carbon Android app
@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun rememberCarbonAndroidAppState(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberAnimatedNavController(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
): CarbonAndroidAppState {
    return remember(navController, coroutineScope) {
        CarbonAndroidAppState(navController, coroutineScope, drawerState, snackbarHostState)
    }
}
@OptIn(ExperimentalMaterial3Api::class)
class CarbonAndroidAppState(
    val navController: NavHostController,
    private val coroutineScope: CoroutineScope,
    val drawerState: DrawerState,
    val snackbarHostState: SnackbarHostState,
) {
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState()
            .value
            ?.destination

    val shouldShowBottomBar: Boolean
        @Composable get() = currentDestination
            ?.route
            ?.let { route -> CarbonScreens.values().first { screen -> route == screen.route }} !=
                CarbonScreens.DesignSystem

    private val shouldShowMenuIcon: Boolean
        get() = navController.currentDestination?.route?.let { route ->
            CarbonScreens.values().find { route == it.route }
        } == CarbonScreens.Home

    fun navigateToCarbonScreen(screen: CarbonScreens) = navigateToRoute(screen.route)

    fun navigateToRoute(
        route: String,
        popUpFirst: Boolean = true,
        popUpRoute: () -> Int = {
            // Default to the start destination of the current graph, otherwise default to the
            // default for the root NavGraph(NavHost)
            navController.currentDestination?.parent?.findStartDestination()?.id ?:
            navController.graph.findStartDestination().id
        }
    ) {
        if (drawerState.isOpen) {
            coroutineScope.launch {
                drawerState.close()
            }
        }
        val navigationOp = navOptions {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            if(popUpFirst) {
                popUpTo(popUpRoute()) {
                    saveState = true
                }
            }
            // Avoid multiple copies of the same destination when
            // re-selecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }
        navController.navigate(route, navigationOp)
    }

    private fun toggleDrawer() {
        coroutineScope.launch {
            if (drawerState.isOpen)
                drawerState.close()
            else
                drawerState.open()
        }
    }

    fun onBack() {
        if (drawerState.isOpen)
            coroutineScope.launch {
                drawerState.close()
            }
        else
            navController.popBackStack()
    }
    fun onNavIconClicked() {
        if (shouldShowMenuIcon)
            toggleDrawer()
        else
            onBack()
    }
}
//endregion
//endregion
