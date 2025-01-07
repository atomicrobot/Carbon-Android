package com.atomicrobot.carbon.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DesignServices
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.atomicrobot.carbon.R

data class ScreenIcon(
    val vectorData: ImageVector,
    @StringRes val iconContentDescription: Int,
)

sealed class CarbonScreens(val title: String, val route: String, val iconData: ScreenIcon) {
    data object Home : CarbonScreens(
        "Home",
        "home",
        ScreenIcon(Icons.Filled.Home, R.string.cont_desc_home_icon),
    )

    data object Settings : CarbonScreens(
        "Settings",
        "settings",
        ScreenIcon(Icons.Filled.Settings, R.string.cont_desc_settings_icon),
    )

    data object Design : CarbonScreens(
        "Design Projects",
        "design",
        ScreenIcon(Icons.Filled.DesignServices, R.string.cont_desc_view_icon),
    )

    data object DeepLink : CarbonScreens(
        "Deep Link",
        "deepLinkPath1",
        ScreenIcon(Icons.Filled.QrCodeScanner, R.string.cont_desc_scanner_icon),
    ) {
        const val TEXT_COLOR = "textColor"
        const val TEXT_SIZE = "textSize"
        const val PATH = "path"

        val routeWithArgs = "deepLink/{$PATH}"

        val arguments =
            listOf(
                navArgument(PATH) {
                    nullable = false
                    type = NavType.StringType
                },
                navArgument(TEXT_COLOR) {
                    nullable = true
                    type = NavType.StringType
                    defaultValue = "black"
                },
                navArgument(TEXT_SIZE) {
                    nullable = true
                    type = NavType.StringType
                    defaultValue = "30"
                },
            )
        val deepLink =
            listOf(
                navDeepLink {
                    uriPattern = "atomicrobot://carbon-android/{$PATH}?textSize={$TEXT_SIZE}&textColor={$TEXT_COLOR}"
                },
                navDeepLink {
                    uriPattern = "http://www.atomicrobot.com/carbon-android/{$PATH}?textSize={$TEXT_SIZE}&textColor={$TEXT_COLOR}"
                },
                navDeepLink {
                    uriPattern = "https://www.atomicrobot.com/carbon-android/{$PATH}?textSize={$TEXT_SIZE}&textColor={$TEXT_COLOR}"
                },
            )
    }

    data object Lumen : CarbonScreens(
        "Lumen",
        "lumen",
        ScreenIcon(Icons.Filled.Home, R.string.lumen_title),
    )

    data object Scanner : CarbonScreens(
        "Scanner",
        "scanner",
        ScreenIcon(Icons.Filled.QrCodeScanner, R.string.cont_desc_scanner_icon),
    )

    data object License : CarbonScreens(
        "License",
        "license",
        ScreenIcon(Icons.Filled.Description, R.string.cont_desc_license_icon),
    )

    data object About : CarbonScreens(
        "About",
        "about",
        ScreenIcon(Icons.Filled.Home, R.string.cont_desc_about_icon),
    )

    data object AboutHtml : CarbonScreens(
        "About HTML",
        "abouthtml",
        ScreenIcon(Icons.Filled.Home, R.string.cont_desc_about_icon),
    )

    data object GitInfo : CarbonScreens(
        "Card Details",
        "gitInfo",
        ScreenIcon(Icons.Filled.Info, R.string.git_info),
    )
}

val appScreens =
    listOf(
        CarbonScreens.Home,
        CarbonScreens.Settings,
        CarbonScreens.Design,
    )

val drawerScreens =
    listOf(
        CarbonScreens.Home,
        CarbonScreens.Settings,
        CarbonScreens.Design,
        CarbonScreens.About,
        CarbonScreens.AboutHtml,
        CarbonScreens.License,
    )

sealed class LumenScreens(
    val title: String,
    val route: String,
    val iconResourceId: Int,
    val iconContentDescription: Int,
) {
    val displayTitle: String
        get() = "Lumen -- $title"

    data object Home : LumenScreens(
        "Home",
        "home",
        iconResourceId = R.drawable.ic_lumen_home_icon,
        iconContentDescription = R.string.cont_desc_home_icon,
    )

    data object Routines : LumenScreens(
        "Routines",
        "routines",
        iconResourceId = R.drawable.ic_lumen_schedule_icon,
        iconContentDescription = R.string.cont_desc_schedule_icon,
    )

    data object Scenes : LumenScreens(
        "Scenes",
        "scene",
        iconResourceId = R.drawable.ic_lumen_scene_icon,
        iconContentDescription = R.string.cont_desc_scene_icon,
    )

    data object Settings : LumenScreens(
        "Settings",
        "settings",
        iconResourceId = R.drawable.ic_lumen_meatball,
        iconContentDescription = R.string.cont_desc_settings_icon,
    )
}

val lumenScreens =
    listOf(
        LumenScreens.Home,
        LumenScreens.Routines,
        LumenScreens.Scenes,
        LumenScreens.Settings,
    )
