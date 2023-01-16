package com.atomicrobot.carbon.ui.navigation

import android.graphics.Color
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.atomicrobot.carbon.navigation.CarbonScreens
import com.atomicrobot.carbon.navigation.appScreens
import com.atomicrobot.carbon.navigation.drawerScreens
import com.atomicrobot.carbon.ui.about.AboutHtmlScreen
import com.atomicrobot.carbon.ui.about.AboutScreen
import com.atomicrobot.carbon.ui.components.BottomNavigationBar
import com.atomicrobot.carbon.ui.components.TopBar
import com.atomicrobot.carbon.ui.deeplink.DeepLinkSampleScreen
import com.atomicrobot.carbon.ui.license.LicenseScreen
import com.atomicrobot.carbon.ui.main.MainScreen
import com.atomicrobot.carbon.ui.settings.SettingsScreen
import com.atomicrobot.carbon.ui.theme.CarbonAndroidTheme
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
    val navBackStackEntry: NavBackStackEntry? by navController.currentBackStackEntryAsState()

    val drawerState = rememberDrawerState(DrawerValue.Closed)

    BackHandler(enabled = drawerState.isOpen) {
        scope.launch {
            drawerState.close()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                content = {
                    Drawer(
                        screens = drawerScreens,
                        onDestinationClicked = { route ->
                            scope.launch {
                                drawerState.close()
                            }
                            if (navController.currentBackStackEntry?.destination?.route != route) {
                                navController.navigate(route) {
                                    popUpTo(navController.graph.startDestinationId)
                                    launchSingleTop = true
                                }
                            }
                        }
                    )
                }
            )
        },
        content = {
            Scaffold(
                topBar = {
                    TopBar(
                        title = appBarTitle(navBackStackEntry),
                        buttonIcon = Icons.Filled.Menu,
                        onButtonClicked = {
                            scope.launch {
                                drawerState.open()
                            }
                        }
                    )
                },
                bottomBar = {
                    BottomNavigationBar(
                        destinations = appScreens,
                        navController = navController,
                        onDestinationClicked = {
                            if (navController.currentBackStackEntry?.destination?.route != it.route) {
                                navController.navigate(it.route) {
                                    // Make sure the back stack only consists of the current graphs main
                                    // destination
                                    popUpTo(CarbonScreens.Home.route) {
                                        saveState = true
                                    }
                                    // Singular instance of destinations
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                },
                snackbarHost = { SnackbarHost(snackbarHostState) }
            ) { innerPadding ->
                NavHost(
                    modifier = Modifier.padding(innerPadding),
                    navController = navController,
                    startDestination = "Main"
                ) {
                    mainFlowGraph(navController, snackbarHostState)
                }
            }
        }
    )
}

/**
 * Nested nav. graph dedicated to displaying 'main' app content.
 */
@Suppress("UNUSED_PARAMETER")
fun NavGraphBuilder.mainFlowGraph(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState
) {
    navigation(startDestination = CarbonScreens.Home.route, route = "Main") {
        composable(CarbonScreens.Home.route) {
            CarbonAndroidTheme {
                MainScreen(snackbarHostState)
            }
        }
        composable(CarbonScreens.Settings.route) {
            CarbonAndroidTheme {
                SettingsScreen()
            }
        }
        composable(
            route = CarbonScreens.DeepLink.routeWithArgs,
            arguments = CarbonScreens.DeepLink.arguments,
            deepLinks = CarbonScreens.DeepLink.deepLink
        ) {
            val textColor = it.arguments?.getString("textColor")
            var color = Color.BLACK
            try {
                color = Color.parseColor(textColor)
            } catch (exception: IllegalArgumentException) {
                Timber.e("Unsupported value for color")
            }
            val textSize = it.arguments?.getString("textSize")
            var size = 30f
            if (!textSize.isNullOrEmpty()) {
                try {
                    size = textSize.toFloat()
                } catch (exception: NumberFormatException) {
                    Timber.e("Unsupported value for size")
                }
            }
            CarbonAndroidTheme {
                DeepLinkSampleScreen(
                    textColor = color,
                    textSize = size
                )
            }
        }
        composable(CarbonScreens.About.route) {
            CarbonAndroidTheme {
                AboutScreen()
            }
        }
        composable(CarbonScreens.AboutHtml.route) {
            CarbonAndroidTheme {
                AboutHtmlScreen()
            }
        }
        composable(CarbonScreens.License.route) {
            CarbonAndroidTheme {
                LicenseScreen()
            }
        }
    }
}

@Composable
fun appBarTitle(navBackStackEntry: NavBackStackEntry?): String {
    return when (navBackStackEntry?.destination?.route) {
        CarbonScreens.Home.route -> CarbonScreens.Home.title
        CarbonScreens.Settings.route -> CarbonScreens.Settings.title
        CarbonScreens.DeepLink.route -> CarbonScreens.DeepLink.title
        CarbonScreens.About.route -> CarbonScreens.About.title
        CarbonScreens.AboutHtml.route -> CarbonScreens.AboutHtml.title
        CarbonScreens.License.route -> CarbonScreens.License.title
        else -> ""
    }
}
