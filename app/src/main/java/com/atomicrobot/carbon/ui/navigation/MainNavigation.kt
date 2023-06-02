package com.atomicrobot.carbon.ui.navigation

import android.graphics.Color
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.atomicrobot.carbon.ui.lumen.navigation.DesignLumenNavigation
import com.atomicrobot.carbon.ui.clickableCards.GitInfoScreen
import com.atomicrobot.carbon.ui.main.MainScreen
import com.atomicrobot.carbon.ui.scanner.ScannerScreen
import com.atomicrobot.carbon.ui.settings.SettingsScreen
import com.atomicrobot.carbon.ui.shell.CarbonShellNavigation
import com.atomicrobot.carbon.ui.theme.CarbonAndroidTheme
import com.atomicrobot.carbon.ui.theme.CarbonShellTheme
import com.atomicrobot.carbon.ui.theme.LightBlurple
import com.atomicrobot.carbon.ui.theme.LumenTheme
import com.atomicrobot.carbon.ui.theme.ScannerTheme
import com.atomicrobot.carbon.ui.theme.White100
import com.atomicrobot.carbon.util.LocalActivity
import com.google.mlkit.vision.barcode.common.Barcode
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val navBackStackEntry: NavBackStackEntry? by navController.currentBackStackEntryAsState()

    val showBottomBar = rememberSaveable { mutableStateOf(true) }
    when (navBackStackEntry?.destination?.route) {
        (CarbonScreens.Design.route) -> {
            showBottomBar.value = false
        }
        CarbonScreens.Lumen.route -> {
            showBottomBar.value = false
        }
        CarbonScreens.Scanner.route -> {
            showBottomBar.value = false
        }
        else -> {
            showBottomBar.value = true
        }
    }

    BackHandler(enabled = scaffoldState.drawerState.isOpen) {
        scope.launch {
            scaffoldState.drawerState.close()
        }
    }
    Scaffold(
        topBar = {
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
        bottomBar = {
            if (showBottomBar.value) {
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
            }
        },
        drawerContent = {
            Drawer(
                screens = drawerScreens,
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
            CarbonAndroidTheme {
                MainScreen(scaffoldState)
            }
        }
        composable(CarbonScreens.Settings.route) {
            CarbonAndroidTheme {
                SettingsScreen()
            }
        }
        composable(CarbonScreens.Design.route) {
            CarbonShellTheme {
                CarbonShellNavigation(navController)
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
        composable(CarbonScreens.Lumen.route) {
            LumenTheme {
                val customTextSelectionColors = TextSelectionColors(
                    handleColor = LightBlurple,
                    backgroundColor = LightBlurple.copy(alpha = 0.4f)
                )
                CompositionLocalProvider(
                    LocalContentColor provides White100,
                    LocalTextSelectionColors provides customTextSelectionColors
                ) {
                    DesignLumenNavigation()
                }
            }
        }
        composable(CarbonScreens.Scanner.route) {
            ScannerTheme {
                val activity = LocalActivity.current
                ScannerScreen {
                    when (it.valueType) {
                        Barcode.TYPE_URL -> {
                            val uri = Uri.parse(it.url!!.url)
                            when {
                                (uri.scheme.equals("atomicrobot") || uri.host?.contains(".atomicrobot.com") == true) -> {
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
        composable(CarbonScreens.GitInfo.route) {
            CarbonAndroidTheme {
                GitInfoScreen()
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
        CarbonScreens.Lumen.route -> CarbonScreens.Lumen.title
        CarbonScreens.Scanner.route -> CarbonScreens.Scanner.title
        CarbonScreens.License.route -> CarbonScreens.License.title
        CarbonScreens.GitInfo.route -> CarbonScreens.GitInfo.title
        else -> ""
    }
}

//@Preview(name = "Github Commit")
//@Composable
//fun CommitItem(
//    @PreviewParameter(CommitPreviewProvider::class, limit = 2) commit: Commit,
//    onCardClicked: (route: String) -> Unit = { _ -> }
//) {
//    val navController: NavHostController = rememberNavController( )
//    var clicked by remember {mutableStateOf(false) }
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp, vertical = 4.dp)
//            .pointerInput(Unit) {
//                detectTapGestures(
//                    onLongPress = {
//
//                        //TODO Figure out navigation please, then it should all start comming together
//
//                        if (navController.currentBackStackEntry?.destination?.route != CarbonScreens.GitInfo.route) {
//                            navController.navigate(CarbonScreens.GitInfo.route) {
//                                popUpTo(navController.graph.startDestinationId)
//                                launchSingleTop = true
//                            }
//                        }
////                        navController.navigate(CarbonScreens.GitInfo.route)
////                        onCardClicked(CarbonScreens.GitInfo.route)
//                        clicked = !clicked
//                        /*TODO make a navigate call to a new screen, That also probably means
//                        *  that all the git stuff we did to the main view model, and main screen
//                        *  should be redone for this new screen.*/
//                    }
//                )
//            },
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp)
//        ) {
//            if(clicked) {
//                //Goal is to navigate to commit details Rather than just display some text
////                GitInfoNavigation()
//                Text("clicked")
//            }
//            else {
//                Text(
//                    text = commit.commitMessage,
//                    fontWeight = FontWeight.Bold,
//                    modifier = Modifier.padding(bottom = 8.dp)
//                )
//                Text(text = stringResource(id = R.string.author_format, commit.author))
//            }
//        }
//    }
//}
