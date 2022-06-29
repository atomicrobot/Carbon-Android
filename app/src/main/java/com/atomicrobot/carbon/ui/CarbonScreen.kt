package com.atomicrobot.carbon.ui

import androidx.compose.ui.graphics.vector.ImageVector

enum class CarbonScreen(
) {
    Splash(),
    Main(),
    DeepLinkPath1();
    companion object {
        fun  fromRoute(route: String?): CarbonScreen =
            when (route?.substringBefore("/")) {
                Splash.name -> Splash
                Main.name -> Main
                DeepLinkPath1.name -> DeepLinkPath1
                null -> Main
                else -> throw IllegalArgumentException("Route $route is not recognized.")
            }
    }
}
