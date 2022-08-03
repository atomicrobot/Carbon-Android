package com.atomicrobot.carbon.ui.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.atomicrobot.carbon.navigation.AppScreens
import com.atomicrobot.carbon.navigation.TopBar
import com.atomicrobot.carbon.ui.components.BottomNavigationBar
import com.atomicrobot.carbon.ui.main.MainScreen
import com.atomicrobot.carbon.ui.settings.Settings
import com.atomicrobot.carbon.ui.splash.SplashScreen
import com.atomicrobot.carbon.ui.splash.SplashViewModel
import org.koin.androidx.compose.getViewModel

private val screens = listOf(
    AppScreens.Home,
    AppScreens.Settings,
)

@Composable
fun MainNavigation(isDeepLinkIntent: Boolean) {
    val viewModel: SplashViewModel = getViewModel()
    val navController = rememberNavController()
    Scaffold(
        topBar =
        {
            // Hide the top app bar during splash screen transition
            if (showAppBar(navController = navController)) {
                TopBar(appBarTitle(navController = navController))
            }
        },
        bottomBar =
        {
            // Hide the bottom bar during splash screen transition
            if (showAppBar(navController = navController)) {
                BottomNavigationBar(
                    navController = navController,
                    destinations = screens,
                    onDestinationClicked = {
                        navController.navigate(it.route) {
                            // Make sure the back stack only consists of the current graphs main
                            // destination
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            // Singular instance of destinations
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = AppScreens.SplashScreen.graph
        ) {
            splashGraph(navController, isDeepLinkIntent, viewModel)
            mainFlowGraph(navController)
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
fun NavGraphBuilder.mainFlowGraph(navController: NavHostController) {
    navigation(startDestination = AppScreens.Home.route, route = AppScreens.Home.graph) {
        composable(AppScreens.Home.route) {
            MainScreen()
        }
        composable(AppScreens.Settings.route) {
            Settings()
        }
    }
}

@Composable
fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}

@Composable
fun showAppBar(navController: NavHostController): Boolean {
    val route = currentRoute(navController = navController)
    return route != null && route != AppScreens.SplashScreen.route
}

@Composable
fun appBarTitle(navController: NavHostController): String {
    return when (currentRoute(navController = navController)) {
        AppScreens.SplashScreen.route -> AppScreens.SplashScreen.title
        AppScreens.Home.route -> AppScreens.Home.title
        AppScreens.Settings.route -> AppScreens.Settings.title
        else -> ""
    }
}
