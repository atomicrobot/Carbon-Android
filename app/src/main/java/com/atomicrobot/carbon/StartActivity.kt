package com.atomicrobot.carbon

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.atomicrobot.carbon.ui.BaseActivity
import com.atomicrobot.carbon.ui.splash.SplashViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class StartActivity : BaseActivity() {
    private val splashViewModel: SplashViewModel by viewModel()

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_start)

        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        val appLinkAction = intent.action
        val appLinkData: Uri? = intent.data
        if (Intent.ACTION_VIEW == appLinkAction) {
            Timber.d("KAB TESTING - appLinkAction detected")
            Timber.d("KAB TESTING - appLinkData = $appLinkData")

            appLinkData?.encodedPath?.also {
                Timber.d("KAB TESTING - appLinkData.encodedPath = ${appLinkData.encodedPath}")
                splashViewModel.setDeepLinkPath(appLinkData.encodedPath)

            }
        }
    }
}