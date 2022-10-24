package com.atomicrobot.carbon.ui.navigation

import android.content.Intent
import android.graphics.Color
import androidx.activity.compose.BackHandler
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
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.atomicrobot.carbon.CarbonShellActivity
import com.atomicrobot.carbon.navigation.CarbonScreens
import com.atomicrobot.carbon.navigation.appScreens
import com.atomicrobot.carbon.ui.components.BottomNavigationBar
import com.atomicrobot.carbon.ui.components.TopBar
import com.atomicrobot.carbon.ui.deeplink.DeepLinkSampleScreen
import com.atomicrobot.carbon.ui.main.MainScreen
import com.atomicrobot.carbon.ui.settings.SettingsScreen
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val navBackStackEntry: NavBackStackEntry? by navController.currentBackStackEntryAsState()
    val context = LocalContext.current

    BackHandler(enabled = scaffoldState.drawerState.isOpen) {
        scope.launch {
            scaffoldState.drawerState.close()
        }
    }
    Scaffold(
        topBar =
        {
            TopBar(
                title = appBarTitle(navBackStackEntry),
                buttonIcon = Icons.Filled.Menu,
                onButtonClicked = {
                    scope.launch {
                        scaffoldState.drawerState.open()
                    }
                }
            )
        },
        bottomBar =
        {
            BottomNavigationBar(
                destinations = appScreens,
                navController = navController,
                onDestinationClicked = {
                    if (navController.currentBackStackEntry?.destination?.route != it.route) {
                        if (it.route == CarbonScreens.Design.route) {
                            context.startActivity(Intent(context, CarbonShellActivity::class.java))
                            navController.popBackStack()
                        } else {
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
                }
            )
        },
        drawerContent =
        {
            Drawer(
                screens = appScreens,
                onDestinationClicked = { route ->
                    scope.launch {
                        scaffoldState.drawerState.close()
                    }
                    if (navController.currentBackStackEntry?.destination?.route != route) {
                        if (route == CarbonScreens.Design.route) {
                            context.startActivity(Intent(context, CarbonShellActivity::class.java))
                            navController.popBackStack()
                        } else {
                            navController.navigate(route) {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
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
            startDestination = "Main"
        ) {
            mainFlowGraph(navController, scaffoldState)
        }
    }
}

/**
 * Nested nav. graph dedicated to displaying 'main' app content.
 */
@Suppress("UNUSED_PARAMETER")
fun NavGraphBuilder.mainFlowGraph(
    navController: NavHostController,
    scaffoldState: ScaffoldState
) {
    navigation(startDestination = CarbonScreens.Home.route, route = "Main") {
        composable(CarbonScreens.Home.route) {
            MainScreen(scaffoldState)
        }
        composable(CarbonScreens.Settings.route) {
            SettingsScreen()
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
            DeepLinkSampleScreen(
                textColor = color,
                textSize = size
            )
        }
    }
}

@Composable
fun appBarTitle(navBackStackEntry: NavBackStackEntry?): String {
    return when (navBackStackEntry?.destination?.route) {
        CarbonScreens.Home.route -> CarbonScreens.Home.title
        CarbonScreens.Settings.route -> CarbonScreens.Settings.title
        else -> ""
    }
}
