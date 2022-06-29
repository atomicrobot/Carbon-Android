package com.atomicrobot.carbon.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.atomicrobot.carbon.ComposeStartActivity
import com.atomicrobot.carbon.ui.deeplink.DeepLinkSampleScreen
import com.atomicrobot.carbon.ui.main.Main
import com.atomicrobot.carbon.ui.main.MainViewModelCompose
import com.atomicrobot.carbon.ui.splash.SplashScreen
import com.atomicrobot.carbon.ui.splash.SplashViewModel
import timber.log.Timber

@Composable
fun MainNavigation(isDeepLinkIntent: Boolean) {
    val viewModel = hiltViewModel<SplashViewModel>()
    val mainViewModel = hiltViewModel<MainViewModelCompose>()
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = ComposeStartActivity.splashPage, modifier = Modifier) {
        composable(ComposeStartActivity.splashPage) {
//            Timber.d("The splash screen recomposed Matthew")
            SplashScreen() {
                if (isDeepLinkIntent) {
                    navController.navigate(viewModel.getDeepLinkNavDestination())
                } else {
                    navController.navigate(ComposeStartActivity.mainPage)
                }
            }
        }
        composable(ComposeStartActivity.mainPage) { Main(mainViewModel) }
        composable(ComposeStartActivity.deepLinkPath1) {
            val textColor = viewModel.getDeepLinkTextColor()
            val textSize = viewModel.getDeepLinkTextSize()
            DeepLinkSampleScreen(textColor, textSize)
        }
    }

}
