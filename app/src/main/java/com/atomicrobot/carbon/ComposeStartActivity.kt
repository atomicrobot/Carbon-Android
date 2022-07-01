package com.atomicrobot.carbon

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.atomicrobot.carbon.ui.compose.MainNavigation
import com.atomicrobot.carbon.ui.deeplink.DeepLinkSampleScreen
import com.atomicrobot.carbon.ui.main.MainScreen
import com.atomicrobot.carbon.ui.main.MainViewModelCompose
import com.atomicrobot.carbon.ui.splash.SplashScreen
import com.atomicrobot.carbon.ui.splash.SplashViewModel
import com.atomicrobot.carbon.ui.theme.CarbonAndroidTheme
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class ComposeStartActivity : ComponentActivity() {
    private val splashViewModel: SplashViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleIntent(intent)


        setContent {
                MainNavigation(false)
        }
    }

    private fun handleIntent(intent: Intent): Boolean {
        val appLinkAction = intent.action
        val appLinkData: Uri? = intent.data
        return if (Intent.ACTION_VIEW == appLinkAction) {
            appLinkData?.encodedPath?.also {
                splashViewModel.setDeepLinkUri(appLinkData)
                splashViewModel.setDeepLinkPath(appLinkData.encodedPath)

                Timber.d("appLinkData = $appLinkData")
                Timber.d("appLinkData.encodedPath = ${appLinkData.encodedPath}")
                Timber.d("appLinkData.query = ${appLinkData.query}")
                Timber.d("appLinkData.encodedQuery = ${appLinkData.encodedQuery}")
                Timber.d("appLinkData.queryParameterNames = ${appLinkData.queryParameterNames}")
            }
            true
        } else false
    }

    companion object {
        const val mainPage = "mainScreen"
        const val splashPage = "splashScreen"
        const val deepLinkPath1 = "deepLinkPath1"
    }
}