package com.atomicrobot.carbon.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.atomicrobot.carbon.StartActivity
import com.atomicrobot.carbon.ui.deeplink.DeepLinkSampleScreen
import com.atomicrobot.carbon.ui.main.MainScreen
import com.atomicrobot.carbon.ui.splash.SplashScreen
import com.atomicrobot.carbon.ui.splash.SplashViewModel

@Composable
fun MainNavigation(isDeepLinkIntent: Boolean) {
    val viewModel: SplashViewModel = hiltViewModel()
    val navController = rememberNavController()

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
                    navController.navigate(StartActivity.mainPage)
                }
            }
        }
        composable(StartActivity.mainPage) { MainScreen() }
        composable(StartActivity.deepLinkPath1) {
            val textColor = viewModel.getDeepLinkTextColor()
            val textSize = viewModel.getDeepLinkTextSize()
            DeepLinkSampleScreen(textColor, textSize)
        }
    }
}
