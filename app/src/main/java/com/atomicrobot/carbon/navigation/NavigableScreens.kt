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

    object Design : CarbonScreens(
        "Design Projects",
        "design",
        ScreenIcon(Icons.Filled.DesignServices, R.string.cont_desc_view_icon)
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

    object Lumen : CarbonScreens(
        "Lumen",
        "lumen",
        ScreenIcon(Icons.Filled.Home, R.string.lumen_title)
    )

    object Scanner : CarbonScreens(
        "Scanner",
        "scanner",
        ScreenIcon(Icons.Filled.QrCodeScanner, R.string.cont_desc_scanner_icon)
    )

    object License : CarbonScreens(
        "License",
        "license",
        ScreenIcon(Icons.Filled.Description, R.string.cont_desc_license_icon)
    )

    object GitInfo : CarbonScreens (
        "Git Info",
        "gitInfo",
        ScreenIcon(Icons.Filled.Info, R.string.git_info)
    )
}

val appScreens = listOf(
    CarbonScreens.Home,
    CarbonScreens.Settings,
    CarbonScreens.Design
)

val drawerScreens = listOf(
    CarbonScreens.Home,
    CarbonScreens.Settings,
    CarbonScreens.Design,
    CarbonScreens.License,
    CarbonScreens.GitInfo
)

sealed class LumenScreens(
    val title: String,
    val route: String,
    val iconResourceId: Int,
    val iconContentDescription: Int
) {

    val displayTitle: String
        get() = "Lumen -- $title"

    object Home : LumenScreens(
        "Home",
        "home",
        iconResourceId = R.drawable.ic_lumen_home_icon,
        iconContentDescription = R.string.cont_desc_home_icon
    )
    object Routines : LumenScreens(
        "Routines", "routines",
        iconResourceId = R.drawable.ic_lumen_schedule_icon,
        iconContentDescription = R.string.cont_desc_schedule_icon
    )
    object Scenes : LumenScreens(
        "Scenes", "scene",
        iconResourceId = R.drawable.ic_lumen_scene_icon,
        iconContentDescription = R.string.cont_desc_scene_icon
    )
    object Settings : LumenScreens(
        "Settings", "settings",
        iconResourceId = R.drawable.ic_lumen_meatball,
        iconContentDescription = R.string.cont_desc_settings_icon
    )
}

val lumenScreens = listOf(
    LumenScreens.Home,
    LumenScreens.Routines,
    LumenScreens.Scenes,
    LumenScreens.Settings,
)
