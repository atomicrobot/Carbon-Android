package com.atomicrobot.carbon.ui.navigation

import android.graphics.Color
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavGraphBuilder
import com.atomicrobot.carbon.navigation.CarbonScreens
import com.atomicrobot.carbon.ui.about.AboutHtmlScreen
import com.atomicrobot.carbon.ui.about.AboutScreen
import com.atomicrobot.carbon.ui.components.CarbonScreen
import com.atomicrobot.carbon.ui.deeplink.DeepLinkSampleScreen
import com.atomicrobot.carbon.ui.license.LicenseScreen
import com.atomicrobot.carbon.ui.main.MainScreen
import com.atomicrobot.carbon.ui.settings.SettingsScreen
import com.google.accompanist.navigation.animation.composable
import timber.log.Timber

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.carbonFlow(
    snackbarHostState: SnackbarHostState,
    onDrawerClicked: () -> Unit,
    onUpdateAppBarState: (AppBarState) -> Unit
){
    composable(CarbonScreens.Home.route) {
        CarbonScreen(
            onUpdateAppBarState = onUpdateAppBarState,
            onDrawerClicked = onDrawerClicked,
            title = CarbonScreens.Home.title
        ){
            MainScreen(snackbarHostState)
        }
    }
    composable(CarbonScreens.Settings.route) {
        CarbonScreen(
            onUpdateAppBarState = onUpdateAppBarState,
            onDrawerClicked = onDrawerClicked,
            title = CarbonScreens.Settings.title
        ){
            SettingsScreen()
        }
    }
    composable(
        route = CarbonScreens.DeepLink.routeWithArgs,
        arguments = CarbonScreens.DeepLink.arguments,
        deepLinks = CarbonScreens.DeepLink.deepLink
    ) {
        val textColor = it.arguments?.getString("textColor")
        var color = Color.BLACK
        try {
            color = Color.parseColor(textColor)
        } catch (exception: IllegalArgumentException) {
            Timber.e("Unsupported value for color")
        }
        val textSize = it.arguments?.getString("textSize")
        var size = 30f
        if (!textSize.isNullOrEmpty()) {
            try {
                size = textSize.toFloat()
            } catch (exception: NumberFormatException) {
                Timber.e("Unsupported value for size")
            }
        }
        CarbonScreen(
            onUpdateAppBarState = onUpdateAppBarState,
            onDrawerClicked = onDrawerClicked,
            title = CarbonScreens.DeepLink.title
        ){
            DeepLinkSampleScreen(
                textColor = color,
                textSize = size
            )
        }
    }
    composable(CarbonScreens.About.route) {
        CarbonScreen(
            onUpdateAppBarState = onUpdateAppBarState,
            onDrawerClicked = onDrawerClicked,
            title = CarbonScreens.About.title
        ){
            AboutScreen()
        }
    }
    composable(CarbonScreens.AboutHtml.route) {
        CarbonScreen(
            onUpdateAppBarState = onUpdateAppBarState,
            onDrawerClicked = onDrawerClicked,
            title = CarbonScreens.AboutHtml.title
        ){
            AboutHtmlScreen()
        }
    }
    composable(CarbonScreens.License.route) {
        CarbonScreen(
            onUpdateAppBarState = onUpdateAppBarState,
            onDrawerClicked = onDrawerClicked,
            title = CarbonScreens.License.title
        ){
            LicenseScreen()
        }
    }
}