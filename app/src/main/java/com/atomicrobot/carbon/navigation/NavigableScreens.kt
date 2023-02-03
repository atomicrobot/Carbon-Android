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

sealed class CarbonScreens(
    @StringRes val title: Int,
    val route: String,
    val iconData: ScreenIcon
) {
    object Home : CarbonScreens(
        R.string.carbon_home,
        "home",
        ScreenIcon(
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            iconContentDescription = R.string.cont_desc_home_icon
        )
    )
    object Settings : CarbonScreens(
        R.string.carbon_settings,
        "settings",
        ScreenIcon(
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings,
            iconContentDescription = R.string.cont_desc_settings_icon
        )
    )
    object DeepLink : CarbonScreens(
        R.string.carbon_deeplink,
        "",
        ScreenIcon(
            selectedIcon = Icons.Filled.QrCodeScanner,
            unselectedIcon = Icons.Outlined.QrCodeScanner,
            iconContentDescription = R.string.cont_desc_scanner_icon
        )
    ) {
        private const val pathParam = "path"
        private const val textColorParam = "textColor"
        private const val textSizeParam = "textSize"

        const val routeWithArgs: String = "deepLink/{$pathParam}?=$textColorParam={$textColorParam}&?=$textSizeParam={$textSizeParam}"

        val arguments = listOf(
            navArgument(pathParam) {
                nullable = false
                type = NavType.StringType
            },
            navArgument(textColorParam) {
                nullable = true
                type = NavType.StringType
                defaultValue = "black"
            },
            navArgument(textSizeParam) {
                nullable = true
                type = NavType.StringType
                defaultValue = "30"
            }
        )
        val deepLink = listOf(
            navDeepLink {
                uriPattern = "atomicrobot://carbon-android/{$pathParam}?textSize={$textSizeParam}&textColor={$textColorParam}"
            },
            navDeepLink {
                uriPattern = "http://www.atomicrobot.com/carbon-android/{$pathParam}?textSize={$textSizeParam}&textColor={$textColorParam}"
            },
            navDeepLink {
                uriPattern = "https://www.atomicrobot.com/carbon-android/{$pathParam}?textSize={$textSizeParam}&textColor={$textColorParam}"
            },
        )

        /**
         * Extracts the encoded text color parameter from a (NavBackStackEntry) navigation event.
         * This is useful for extracting the encoded text color parameter from a deep-linking
         * intent.
         * @param defaultTextColor The value returned if there isn't a text color parameter in the
         * NavBackStackEntry.
         */
        fun NavBackStackEntry.getTextColor(defaultTextColor: Int = Color.BLACK): Int {
            return try {
                val colorStr = arguments?.getString(textColorParam)
                if (!colorStr.isNullOrEmpty()) {
                    Color.parseColor(colorStr)
                } else defaultTextColor
            } catch (exception: IllegalArgumentException) {
                Timber.e("Unsupported value for color")
                defaultTextColor
            }
        }

        /**
         * Extracts the encoded text size parameter from a (NavBackStackEntry) navigation event.
         * This is useful for extracting the encoded text size parameter from a deep-linking intent.
         * @param defaultTextSize The value returned if there isn't a text size parameter in the
         * NavBackStackEntry.
         */
        fun NavBackStackEntry.getTextSize(defaultTextSize: Float = 30f): Float {
            return try {
                val textSizeStr = arguments?.getString(textSizeParam)
                if (!textSizeStr.isNullOrEmpty()) {
                    textSizeStr.toFloat()
                } else defaultTextSize
            } catch (exception: NumberFormatException) {
                Timber.e("Unsupported value for size")
                defaultTextSize
            }
        }

        /**
         * Extracts the encoded path parameter from a (NavBackStackEntry) navigation event.
         * This is useful for extracting the encoded path parameter from a deep-linking intent.
         * @param defaultPath The value returned if there isn't a path parameter in the
         * NavBackStackEntry.
         */
        @Suppress("unused")
        fun NavBackStackEntry.getEncodedPath(defaultPath: String): String {
            val encodedPath = arguments?.getString(pathParam)
            return encodedPath.takeIf { !it.isNullOrEmpty() } ?: defaultPath
        }
    }

    object License : CarbonScreens(
        R.string.carbon_license,
        "license",
        ScreenIcon(
            selectedIcon = Icons.Filled.Description,
            unselectedIcon = Icons.Outlined.Description,
            iconContentDescription = R.string.cont_desc_license_icon
        )
    )
    object About : CarbonScreens(
        R.string.carbon_about,
        "about",
        ScreenIcon(
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            iconContentDescription = R.string.cont_desc_about_icon
        ) // Icon value here is a filler
    )
    object AboutHtml : CarbonScreens(
        R.string.carbon_about_html,
        "abouthtml",
        ScreenIcon(
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            iconContentDescription = R.string.cont_desc_about_icon
        ) // Icon value here is a filler
    )
    object DesignSystem : CarbonScreens(
        R.string.design_home,
        "design",
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

val drawerScreens = listOf(
    CarbonScreens.Home,
    CarbonScreens.Settings,
    CarbonScreens.About,
    CarbonScreens.AboutHtml,
    CarbonScreens.License,
    CarbonScreens.DesignSystem,
)
