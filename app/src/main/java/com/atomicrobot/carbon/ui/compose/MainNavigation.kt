package com.atomicrobot.carbon.ui.compose

import android.graphics.Color
import android.net.Uri
import android.widget.Toast
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
import androidx.navigation.NavBackStackEntry
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
import com.atomicrobot.carbon.ui.deeplink.DeepLinkSampleScreen
import com.atomicrobot.carbon.ui.main.MainScreen
import com.atomicrobot.carbon.ui.scanner.ScannerScreen
import com.atomicrobot.carbon.ui.settings.Settings
import com.atomicrobot.carbon.ui.splash.SplashViewModel
import com.google.mlkit.vision.barcode.common.Barcode
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import timber.log.Timber

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

    BackHandler(enabled = scaffoldState.drawerState.isOpen) {
        scope.launch {
            scaffoldState.drawerState.close()
        }
    }
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
                                popUpTo(AppScreens.Home.route) {
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
            startDestination = "Main"
        ) {
            mainFlowGraph(navController, scaffoldState, isDeepLinkIntent, viewModel)
        }
    }
}

/**
 * Nested nav. graph dedicated to displaying 'main' app content.
 */
@Suppress("UNUSED_PARAMETER")
fun NavGraphBuilder.mainFlowGraph(
    navController: NavHostController,
    scaffoldState: ScaffoldState,
    isDeepLinkIntent: Boolean,
    viewModel: SplashViewModel
) {
    navigation(startDestination = AppScreens.Home.route, route = "Main") {
        composable(AppScreens.Home.route) {
            MainScreen(scaffoldState)
        }
        composable(AppScreens.Settings.route) {
            Settings()
        }
        composable(AppScreens.Scanner.route) {
            val activity = LocalActivity.current
            ScannerScreen(scaffoldState) {
                when (it.valueType) {
                    Barcode.TYPE_URL -> {
                        val uri = Uri.parse(it.url!!.url)
                        when {
                            (
                                uri.scheme.equals("atomicrobot") ||
                                    uri.host?.contains(".atomicrobot.com") == true
                                ) -> {
                                navController.navigate(uri)
                                return@ScannerScreen
                            }
                        }
                    }
                    else -> { /* Intentionally left blank */ }
                }
                Toast.makeText(
                    activity,
                    "Barcode clicked: ${it.displayValue}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        composable(
            route = AppScreens.DeepLink.routeWithArgs,
            arguments = AppScreens.DeepLink.arguments,
            deepLinks = AppScreens.DeepLink.deepLink
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
