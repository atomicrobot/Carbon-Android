package com.atomicrobot.carbon.ui.compose

import androidx.compose.material.DrawerValue
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalDrawer
import androidx.compose.material.Surface
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.atomicrobot.carbon.StartActivity
import com.atomicrobot.carbon.navigation.AppScreens
import com.atomicrobot.carbon.navigation.Drawer
import com.atomicrobot.carbon.ui.deeplink.DeepLinkSampleScreen
import com.atomicrobot.carbon.ui.main.MainScreen
import com.atomicrobot.carbon.ui.settings.Settings
import com.atomicrobot.carbon.ui.splash.SplashScreen
import com.atomicrobot.carbon.ui.splash.SplashViewModel
import com.atomicrobot.carbon.ui.theme.CarbonAndroidTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@Composable
fun MainNavigation(isDeepLinkIntent: Boolean) {
    val viewModel: SplashViewModel = getViewModel()
    val navController = rememberNavController()

    Surface(color = MaterialTheme.colors.background) {
        val drawerState = rememberDrawerState(DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        val openDrawer = {
            scope.launch {
                drawerState.open()
            }
        }
        ModalDrawer(
            drawerState = drawerState,
            gesturesEnabled = drawerState.isOpen,
            drawerContent = {
                Drawer(
                    onDestinationClicked = { route ->
                        scope.launch {
                            drawerState.close()
                        }
                        navController.navigate(route) {
                            launchSingleTop = true
                        }
                    }
                )
            }
        ) {
            NavHost(
                navController = navController,
                startDestination = AppScreens.SplashScreen.route
            ) {
                composable(AppScreens.Home.route) {
                    MainScreen(
                        openDrawer = {
                            openDrawer()
                        }
                    )
                }
                composable(AppScreens.Settings.route) {
                    Settings(
                        navController = navController
                    )
                }
                composable(AppScreens.SplashScreen.route) {
                    SplashScreen {
                        navController.popBackStack()
                        if (isDeepLinkIntent) {
                            navController.navigate(viewModel.getDeepLinkNavDestination())
                        } else {
                            navController.navigate(AppScreens.Home.route)
                        }
                    }
                }
            }
        }
    }
}
