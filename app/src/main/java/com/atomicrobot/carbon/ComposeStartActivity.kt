package com.atomicrobot.carbon

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.atomicrobot.carbon.ui.ComposeBaseActivity
import com.atomicrobot.carbon.ui.compose.MainNavigation
import com.atomicrobot.carbon.ui.splash.SplashViewModel
import com.atomicrobot.carbon.ui.theme.CarbonAndroidTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ComposeStartActivity : ComposeBaseActivity() {
    private val splashViewModel: SplashViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
            .detectNetwork()
            .penaltyLog()
            .build())
        super.onCreate(savedInstanceState)

        setContent {
            CarbonAndroidTheme {
                MainNavigation(true)
            }
        }
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent): Boolean {
        Timber.d("handle intent")
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