package com.atomicrobot.carbon.ui.navigation

import android.graphics.Color
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
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
import com.atomicrobot.carbon.ui.components.CustomSnackbar
import com.atomicrobot.carbon.ui.components.NavigationTopBar
import com.atomicrobot.carbon.ui.deeplink.DeepLinkSampleScreen
import com.atomicrobot.carbon.ui.designsystem.DesignSystemActivity
import com.atomicrobot.carbon.ui.license.LicenseScreen
import com.atomicrobot.carbon.ui.main.MainScreen
import com.atomicrobot.carbon.ui.settings.SettingsScreen
import com.atomicrobot.carbon.ui.theme.CarbonAndroidTheme
import com.atomicrobot.carbon.util.LocalActivity
import com.atomicrobot.carbon.util.startComponentActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

//region Composables
@Composable
fun CarbonApp() {
    CarbonAndroidTheme {
        if(LocalContext.current is ComponentActivity) {
            // Wrap the composable in a LocalActivity provider so our composable 'environment'
            // has access to Activity context/scope which is required for requesting permissions
            val parentActivity = LocalContext.current as ComponentActivity
            CompositionLocalProvider(LocalActivity provides parentActivity) {
                MainNavigation()
            }
        }
        else {
            MainNavigation()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavigation() {
    val scope: CoroutineScope = rememberCoroutineScope()
    val drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed)
    val navController: NavHostController = rememberNavController()
    BackHandler(enabled = drawerState.isOpen) {
        scope.launch {
            drawerState.close()
        }
    }

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                DrawerContent(scope, navController, drawerState)
            }
        },
        drawerState = drawerState,
    ) {
        MainContent(scope, navController, drawerState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerContent(scope: CoroutineScope, navController: NavController, drawerState: DrawerState) {
    val parentActivity = LocalActivity.current
    Drawer(
        screens = drawerScreens,
        onDestinationClicked = { route ->
            scope.launch {
                drawerState.close()
            }
            if (navController.currentBackStackEntry?.destination?.route != route) {
                when(route) {
                    CarbonScreens.DesignSystem.route -> {
                        parentActivity.startComponentActivity<DesignSystemActivity>()
                    }
                    else -> {
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(scope: CoroutineScope, navController: NavHostController, drawerState: DrawerState) {
    val navBackStackEntry: NavBackStackEntry? by navController.currentBackStackEntryAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val parentActivity = LocalActivity.current
    Scaffold(
        topBar = {
            NavigationTopBar(
                title = appBarTitle(navBackStackEntry),
                navigationIcon = Icons.Filled.Menu,
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
                        when(it.route) {
                            CarbonScreens.DesignSystem.route -> {
                                parentActivity.startComponentActivity<DesignSystemActivity>()
                            }
                            else -> {
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
                }
            )
        },
        snackbarHost = { CustomSnackbar(snackbarHostState) },
    ) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = "Main"
        ) {
            mainFlowGraph(snackbarHostState)
        }
    }
}

/**
 * Nested nav. graph dedicated to displaying 'main' app content.
 */
@Suppress("UNUSED_PARAMETER")
fun NavGraphBuilder.mainFlowGraph(
    snackbarHostState: SnackbarHostState,
) {
    navigation(startDestination = CarbonScreens.Home.route, route = "Main") {
        composable(CarbonScreens.Home.route) {
            MainScreen(snackbarHostState)
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
        composable(CarbonScreens.About.route) {
            AboutScreen()
        }
        composable(CarbonScreens.AboutHtml.route) {
            AboutHtmlScreen()
        }
        composable(CarbonScreens.License.route) {
            LicenseScreen(snackbarHostState)
        }
    }
}

@Composable
fun appBarTitle(navBackStackEntry: NavBackStackEntry?): String {
    return when (navBackStackEntry?.destination?.route) {
        CarbonScreens.Home.route -> CarbonScreens.Home.title
        CarbonScreens.Settings.route -> CarbonScreens.Settings.title
        CarbonScreens.DeepLink.route -> CarbonScreens.DeepLink.title
        CarbonScreens.License.route -> CarbonScreens.License.title
        else -> ""
    }
}
//endregion

//region Composable Previews
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun DrawerContentPreview() {
    CarbonAndroidTheme {
        val scope: CoroutineScope = rememberCoroutineScope()
        val drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed)
        val navController: NavHostController = rememberNavController()
        DrawerContent(scope = scope, navController = navController, drawerState = drawerState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun MainContentPreview() {
    CarbonAndroidTheme {
        val scope: CoroutineScope = rememberCoroutineScope()
        val drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed)
        val navController: NavHostController = rememberNavController()
        MainContent(scope = scope, navController = navController, drawerState = drawerState)
    }
}
//endregion