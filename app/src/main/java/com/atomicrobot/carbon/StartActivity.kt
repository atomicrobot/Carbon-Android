package com.atomicrobot.carbon

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.atomicrobot.carbon.ui.navigation.MainNavigation
import com.atomicrobot.carbon.ui.splash.SplashViewModel
import com.atomicrobot.carbon.ui.theme.CarbonAndroidTheme
import com.atomicrobot.carbon.util.LocalActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class StartActivity : ComponentActivity() {
    private val splashViewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        handleIntent(intent)

        setContent {
            CarbonAndroidTheme {
                // Wrap the composable in a LocalActivity provider so our composable 'environment'
                // has access to Activity context/scope which is required for requesting permissions
                CompositionLocalProvider(LocalActivity provides this) {
                    MainNavigation()
                }
            }
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
        const val deepLinkPath1 = "deepLinkPath1"
    }
}