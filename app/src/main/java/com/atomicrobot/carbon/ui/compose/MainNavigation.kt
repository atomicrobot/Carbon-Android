package com.atomicrobot.carbon.ui.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarHost
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.atomicrobot.carbon.navigation.AppScreens
import com.atomicrobot.carbon.navigation.Drawer
import com.atomicrobot.carbon.navigation.TopBar
import com.atomicrobot.carbon.ui.components.BottomNavigationBar
import com.atomicrobot.carbon.ui.main.MainScreen
import com.atomicrobot.carbon.ui.scanner.ScannerScreen
import com.atomicrobot.carbon.ui.settings.Settings
import com.atomicrobot.carbon.ui.splash.SplashScreen
import com.atomicrobot.carbon.ui.splash.SplashViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

private val screens = listOf(
    AppScreens.Home,
    AppScreens.Settings,
    AppScreens.Scanner
)

@Composable
fun MainNavigation(isDeepLinkIntent: Boolean) {
    val viewModel: SplashViewModel = getViewModel()
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val navBackStackEntry: NavBackStackEntry? by navController.currentBackStackEntryAsState()
    Scaffold(
        topBar =
        {
            // Hide the top app bar during splash screen transition
            if (showAppBar(navBackStackEntry)) {
                TopBar(
                    title = appBarTitle(navBackStackEntry),
                    buttonIcon = Icons.Filled.Menu,
                    onButtonClicked = {
                        scope.launch {
                            scaffoldState.drawerState.open()
                        }
                    }
                )
            }
        },
        bottomBar =
        {
            // Hide the bottom bar during splash screen transition
            if (showAppBar(navBackStackEntry)) {
                BottomNavigationBar(
                    navController = navController,
                    destinations = screens,
                    onDestinationClicked = {
                        if (navController.currentBackStackEntry?.destination?.route != it.route) {
                            navController.navigate(it.route) {
                                // Make sure the back stack only consists of the current graphs main
                                // destination
                                popUpTo(AppScreens.Home.graph) {
                                    saveState = true
                                }
                                // Singular instance of destinations
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
        },
        drawerContent =
        {
            Drawer(
                screens = screens,
                onDestinationClicked = { route ->
                    scope.launch {
                        scaffoldState.drawerState.close()
                    }
                    if (navController.currentBackStackEntry?.destination?.route != route) {
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(scaffoldState.snackbarHostState) },
        scaffoldState = scaffoldState
    ) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = AppScreens.SplashScreen.graph
        ) {
            splashGraph(navController, isDeepLinkIntent, viewModel)
            mainFlowGraph(navController, scaffoldState)
        }
    }
}

/*
 * Nested nav. graph dedicated to displaying Splash related content
 */
fun NavGraphBuilder.splashGraph(
    navController: NavController,
    isDeepLinkIntent: Boolean,
    viewModel: SplashViewModel
) {
    navigation(AppScreens.SplashScreen.route, AppScreens.SplashScreen.graph) {
        composable(AppScreens.SplashScreen.route) {
            SplashScreen {
                navController.popBackStack()
                if (isDeepLinkIntent) {
                    navController.navigate(viewModel.getDeepLinkNavDestination())
                } else {
                    navController.navigate(AppScreens.Home.graph)
                }
            }
        }
    }
}

/**
 * Nested nav. graph dedicated to displaying 'main' app content.
 */
@Suppress("UNUSED_PARAMETER")
fun NavGraphBuilder.mainFlowGraph(navController: NavHostController, scaffoldState: ScaffoldState) {
    navigation(startDestination = AppScreens.Home.route, route = AppScreens.Home.graph) {
        composable(AppScreens.Home.route) {
            MainScreen(scaffoldState)
        }
        composable(AppScreens.Settings.route) {
            Settings()
        }
        composable(AppScreens.Scanner.route) {
            ScannerScreen(scaffoldState)
        }
    }
}

@Composable
fun showAppBar(navBackStackEntry: NavBackStackEntry?): Boolean {
    return navBackStackEntry?.destination?.route != AppScreens.SplashScreen.route
}

@Composable
fun appBarTitle(navBackStackEntry: NavBackStackEntry?): String {
    return when (navBackStackEntry?.destination?.route) {
        AppScreens.SplashScreen.route -> AppScreens.SplashScreen.title
        AppScreens.Home.route -> AppScreens.Home.title
        AppScreens.Settings.route -> AppScreens.Settings.title
        AppScreens.Scanner.route -> AppScreens.Scanner.title
        else -> ""
    }
}
