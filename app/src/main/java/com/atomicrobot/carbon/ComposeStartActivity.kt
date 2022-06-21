package com.atomicrobot.carbon

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import com.atomicrobot.carbon.ui.ComposeBaseActivity
import com.atomicrobot.carbon.ui.deeplink.DeepLinkPath1
import com.atomicrobot.carbon.ui.deeplink.DeepLinkSampleViewModel
import com.atomicrobot.carbon.ui.main.Main
import com.atomicrobot.carbon.ui.main.MainViewModelCompose
import com.atomicrobot.carbon.ui.splash.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ComposeStartActivity : ComposeBaseActivity() {
    private val viewModelCompose: MainViewModelCompose by viewModels()
    private val splashViewModel: SplashViewModel by viewModels()
    private val deepLinkSampleViewModel: DeepLinkSampleViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            Main(mainViewModelCompose = viewModelCompose)
        }
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        val appLinkAction = intent.action
        val appLinkData: Uri? = intent.data
        if (Intent.ACTION_VIEW == appLinkAction) {
            appLinkData?.encodedPath?.also {
                splashViewModel.setDeepLinkUri(appLinkData)
                splashViewModel.setDeepLinkPath(appLinkData.encodedPath)

                Timber.d("appLinkData = $appLinkData")
                Timber.d("appLinkData.encodedPath = ${appLinkData.encodedPath}")
                Timber.d("appLinkData.query = ${appLinkData.query}")
                Timber.d("appLinkData.encodedQuery = ${appLinkData.encodedQuery}")
                Timber.d("appLinkData.queryParameterNames = ${appLinkData.queryParameterNames}")
            }
        }
    }
}