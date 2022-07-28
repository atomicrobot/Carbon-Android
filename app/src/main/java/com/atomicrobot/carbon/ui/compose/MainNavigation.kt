package com.atomicrobot.carbon.ui.compose

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.atomicrobot.carbon.StartActivity
import com.atomicrobot.carbon.ui.deeplink.DeepLinkSampleScreen
import com.atomicrobot.carbon.ui.main.MainScreen
import com.atomicrobot.carbon.ui.main.MainViewModel
import com.atomicrobot.carbon.ui.permissions.NotificationsPermissionsScreen
import com.atomicrobot.carbon.ui.permissions.NotificationsPermissionsViewModel
import com.atomicrobot.carbon.ui.splash.SplashScreen
import com.atomicrobot.carbon.ui.splash.SplashViewModel

@Composable
fun MainNavigation(isDeepLinkIntent: Boolean) {
    val viewModel = hiltViewModel<SplashViewModel>()
    val mainViewModel = hiltViewModel<MainViewModel>()
    val notificationsPermissionsViewModel = hiltViewModel<NotificationsPermissionsViewModel>()
    val navController = rememberNavController()
    val activity = LocalContext.current as AppCompatActivity

    NavHost(
        navController = navController,
        startDestination = StartActivity.splashPage,
        modifier = Modifier
    ) {
        composable(StartActivity.splashPage) {
            SplashScreen() {
                navController.popBackStack()
                if (isDeepLinkIntent) {
                    navController.navigate(viewModel.getDeepLinkNavDestination())
                } else {
                    if ( notificationsPermissionsViewModel.notificationsPermissionsGranted() ) {
//                    if (NotificationManagerCompat.from(activity).areNotificationsEnabled()) {
                        navController.navigate(StartActivity.mainPage)
                    } else {
                        navController.navigate(StartActivity.notificationsPermissions) {
                            navController.navigate(StartActivity.mainPage)
                        }
                    }
                }
            }
        }
        composable(StartActivity.mainPage) { MainScreen(mainViewModel) }
        composable(StartActivity.deepLinkPath1) {
            val textColor = viewModel.getDeepLinkTextColor()
            val textSize = viewModel.getDeepLinkTextSize()
            DeepLinkSampleScreen(textColor, textSize)
        }
        composable(StartActivity.notificationsPermissions) {
            NotificationsPermissionsScreen(
                activity = activity,
                notificationsPermissionsViewModel = notificationsPermissionsViewModel,
                onPermissionGranted = {
                    navController.popBackStack()
                    navController.navigate(StartActivity.mainPage)
                },
                onPermissionDenied = {
                    navController.popBackStack()
                    navController.navigate(StartActivity.mainPage)
                },
                onPermissionDelayed = {
                    navController.popBackStack()
                    navController.navigate(StartActivity.mainPage)
                })
        }
    }
}
