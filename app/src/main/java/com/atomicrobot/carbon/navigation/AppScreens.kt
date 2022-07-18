package com.atomicrobot.carbon.navigation

sealed class AppScreens(val title: String, val route: String) {
    object Home : AppScreens("Home", "home")
    object Settings : AppScreens("Settings", "settings")
    object SplashScreen : AppScreens("Splash", "splash")
}