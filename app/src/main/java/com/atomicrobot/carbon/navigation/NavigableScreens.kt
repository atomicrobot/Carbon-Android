package com.atomicrobot.carbon.navigation

import android.graphics.Color
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ImagesearchRoller
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ImagesearchRoller
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.atomicrobot.carbon.R
import timber.log.Timber

data class ScreenIcon(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector = selectedIcon,
    @StringRes val iconContentDescription: Int
)

sealed class CarbonScreens(val title: String, val route: String, val iconData: ScreenIcon) {

    object Home : CarbonScreens(
        "Home",
        "home",
        ScreenIcon(
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            iconContentDescription = R.string.cont_desc_home_icon
        )
    )

    object Settings : CarbonScreens(
        "Settings",
        "settings",
        ScreenIcon(
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings,
            iconContentDescription = R.string.cont_desc_settings_icon
        )
    )

    object DeepLink : CarbonScreens(
        "Deep Link",
        "deepLinkPath1",
        ScreenIcon(
            selectedIcon = Icons.Filled.QrCodeScanner,
            unselectedIcon = Icons.Outlined.QrCodeScanner,
            iconContentDescription = R.string.cont_desc_scanner_icon
        )
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

        fun NavBackStackEntry.getTextColor(defaultColor: Int = Color.BLACK): Int {
            return try{
                val colorStr = arguments?.getString(textColor)
                if(!colorStr.isNullOrEmpty()) {
                    Color.parseColor(colorStr)
                } else defaultColor
            }
            catch (exception: IllegalArgumentException) {
                Timber.e("Unsupported value for color")
                defaultColor
            }
        }
        fun NavBackStackEntry.getTextSize(defaultTextSize: Float = 30f): Float {
            return try{
                val textSizeStr = arguments?.getString(textSize)
                if(!textSizeStr.isNullOrEmpty()) {
                    textSizeStr.toFloat()
                } else defaultTextSize
            }
            catch (exception: NumberFormatException) {
                Timber.e("Unsupported value for size")
                defaultTextSize
            }
        }
    }

    object License : CarbonScreens(
        "License",
        "license",
        ScreenIcon(
            selectedIcon = Icons.Filled.Description,
            unselectedIcon = Icons.Outlined.Description,
            iconContentDescription = R.string.cont_desc_license_icon
        )
    )

    object About : CarbonScreens(
        "About",
        "about",
        ScreenIcon(
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            iconContentDescription = R.string.cont_desc_about_icon
        ) // Icon value here is a filler
    )

    object AboutHtml : CarbonScreens(
        "About HTML",
        "abouthtml",
        ScreenIcon(
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            iconContentDescription = R.string.cont_desc_about_icon
        ) // Icon value here is a filler
    )

     object DesignSystem : CarbonScreens(
        "Design",
        "design_system",
        ScreenIcon(
            selectedIcon = Icons.Filled.ImagesearchRoller,
            unselectedIcon = Icons.Outlined.ImagesearchRoller,
            iconContentDescription = R.string.cont_desc_design_sys
        ),
    )
    companion object {
        fun values(): List<CarbonScreens> =
            CarbonScreens::class.sealedSubclasses.map {
                it.objectInstance as CarbonScreens
            }
    }
}

val appScreens = listOf(
    CarbonScreens.Home,
    CarbonScreens.Settings
)

val drawerScreens = listOf(
    CarbonScreens.Home,
    CarbonScreens.Settings,
    CarbonScreens.About,
    CarbonScreens.AboutHtml,
    CarbonScreens.License,
    CarbonScreens.DesignSystem,
)