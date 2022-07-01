package com.atomicrobot.carbon.ui.compose

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.atomicrobot.carbon.ComposeStartActivity
import com.atomicrobot.carbon.ui.deeplink.DeepLinkSampleScreen
import com.atomicrobot.carbon.ui.main.MainScreen
import com.atomicrobot.carbon.ui.splash.SplashScreen
import com.atomicrobot.carbon.ui.splash.SplashViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun MainNavigation(isDeepLinkIntent: Boolean) {
    val viewModel: SplashViewModel = getViewModel()
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splashScreen") {
        composable("splashScreen") {
            SplashScreen() {
                navController.popBackStack()
                if (isDeepLinkIntent) {
                    navController.navigate(viewModel.getDeepLinkNavDestination())
                } else {
                    navController.navigate("mainScreen")
                }
            }
        }
        composable("mainScreen") { MainScreen() }
        composable(ComposeStartActivity.deepLinkPath1) {
            val textColor = viewModel.getDeepLinkTextColor()
            val textSize = viewModel.getDeepLinkTextSize()
            DeepLinkSampleScreen(textColor, textSize)
        }
    }

}