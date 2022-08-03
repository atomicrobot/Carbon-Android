package com.atomicrobot.carbon.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.atomicrobot.carbon.R

sealed class AppScreens(val title: String, val route: String, val iconData: ScreenIcon) {
    val graph: String
        get() = "graph/${this.route}"

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
}

data class ScreenIcon(val icon: ImageVector, @StringRes val iconContentDescription: Int)
