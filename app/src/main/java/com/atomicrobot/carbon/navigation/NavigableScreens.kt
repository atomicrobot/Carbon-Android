package com.atomicrobot.carbon.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.atomicrobot.carbon.R

data class ScreenIcon(
    val vectorData: ImageVector,
    @StringRes val iconContentDescription: Int
)

sealed class CarbonScreens(val title: String, val route: String, val iconData: ScreenIcon) {

    object Home : CarbonScreens(
        "Home",
        "home",
        ScreenIcon(Icons.Filled.Home, R.string.cont_desc_home_icon)
    )

    object Settings : CarbonScreens(
        "Settings",
        "settings",
        ScreenIcon(Icons.Filled.Settings, R.string.cont_desc_settings_icon)
    )

    object DeepLink : CarbonScreens(
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

    object License : CarbonScreens(
        "License",
        "license",
        ScreenIcon(Icons.Filled.Description, R.string.cont_desc_license_icon)
    )

    object About : CarbonScreens(
        "About",
        "about",
        ScreenIcon(Icons.Filled.Home, R.string.cont_desc_about_icon) // Icon value here is a filler
    )

    object AboutHtml : CarbonScreens(
        "About HTML",
        "abouthtml",
        ScreenIcon(Icons.Filled.Home, R.string.cont_desc_about_icon) // Icon value here is a filler
    )

    object DesignSystems : CarbonScreens(
        "Design System",
        "designSystems",
        ScreenIcon(Icons.Filled.Build, R.string.cont_desc_design_systems) // Icon value here is a filler
    )

    object DesignSystemsDetail : CarbonScreens(
        "Design System Detail",
        "designSystemsDetail/{category}",
        ScreenIcon(Icons.Filled.Build, R.string.cont_desc_design_systems) // Icon value here is a filler
    )
}

val appScreens = listOf(
    CarbonScreens.Home,
    CarbonScreens.Settings
)

val drawerScreens = listOf(
    CarbonScreens.Home,
    CarbonScreens.Settings,
    CarbonScreens.DesignSystems,
    CarbonScreens.About,
    CarbonScreens.AboutHtml,
    CarbonScreens.License,
)
