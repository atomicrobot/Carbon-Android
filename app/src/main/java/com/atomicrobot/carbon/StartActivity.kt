package com.atomicrobot.carbon

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.atomicrobot.carbon.ui.compose.MainNavigation
import com.atomicrobot.carbon.ui.permissions.NotificationsPermissionsViewModel
import com.atomicrobot.carbon.ui.splash.SplashViewModel
import com.atomicrobot.carbon.ui.theme.CarbonAndroidTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class StartActivity : AppCompatActivity() {
    private val notificationsPermissionsViewModel: NotificationsPermissionsViewModel by viewModels()
    private val splashViewModel: SplashViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val isDeepLinkIntent = handleIntent(intent)

        setContent {
            CarbonAndroidTheme {
                MainNavigation(isDeepLinkIntent)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        if ( notificationsPermissionsViewModel.uiState.value == NotificationsPermissionsViewModel.UiState.Unknown) {
            if (NotificationManagerCompat.from(this).areNotificationsEnabled()) {
                notificationsPermissionsViewModel.setState(NotificationsPermissionsViewModel.UiState.Granted)
            } else {
                var shouldShowNotificationsNotEnabled = true

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                    shouldShowNotificationsNotEnabled = shouldShowRequestPermissionRationale(
                        Manifest.permission.POST_NOTIFICATIONS
                    )
                }

                if (shouldShowNotificationsNotEnabled) {
                    notificationsPermissionsViewModel.setState(NotificationsPermissionsViewModel.UiState.Denied)
                } else {
                    notificationsPermissionsViewModel.setState(NotificationsPermissionsViewModel.UiState.Initial)
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
        const val splashPage = "splashScreen"
        const val deepLinkPath1 = "deepLinkPath1"
        const val notificationsPermissions = "notificationsPermissions"
    }
}