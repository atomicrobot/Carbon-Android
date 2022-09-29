package com.atomicrobot.carbon.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
//import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.atomicrobot.carbon.R

sealed class AppScreens(val title: String, val route: String, val iconData: ScreenIcon) {

    object Home : AppScreens(
        "Home",
        "home",
        ScreenIcon(Icons.Filled.Home, R.string.cont_desc_home_icon)
    )

    object Settings : AppScreens(
        "Settings",
        "settings",
        ScreenIcon(Icons.Filled.Settings, R.string.cont_desc_settings_icon)
    )

    object SplashScreen : AppScreens(
        "Splash",
        "splash",
        ScreenIcon(Icons.Filled.Build, R.string.cont_desc_splash_icon)
    )

    object Scanner : AppScreens(
        "Scanner",
        "scanner",
        ScreenIcon(Icons.Filled.QrCodeScanner, R.string.cont_desc_scanner_icon)
    )

    object DeepLink : AppScreens(
        "Deep Link",
        "deepLinkPath1",
        ScreenIcon(Icons.Filled.QrCodeScanner, R.string.cont_desc_scanner_icon)
    ) {

        const val textColor = "textColor"
        const val textSize = "textSize"
        const val path = "path"

        val routeWithArgs = "deepLink/{$path}"

        val arguments = listOf(
            navArgument(path) {
                nullable = false
                type = NavType.StringType
            },
            navArgument(textColor) {
                nullable = true
                type = NavType.StringType
                defaultValue = "black"
            },
            navArgument(textSize) {
                nullable = true
                type = NavType.StringType
                defaultValue = "30"
            }
        )
        val deepLink = listOf(
            navDeepLink {
                uriPattern = "atomicrobot://carbon-android/{$path}?textSize={$textSize}&textColor={$textColor}"
            },
            navDeepLink {
                uriPattern = "http://www.atomicrobot.com/carbon-android/{$path}?textSize={$textSize}&textColor={$textColor}"
            },
            navDeepLink {
                uriPattern = "https://www.atomicrobot.com/carbon-android/{$path}?textSize={$textSize}&textColor={$textColor}"
            },
        )
    }
}

data class ScreenIcon(val icon: ImageVector, @StringRes val iconContentDescription: Int)
