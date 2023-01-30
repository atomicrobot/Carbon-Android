package com.atomicrobot.carbon.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.*
import com.atomicrobot.carbon.ui.theme.CarbonPalette.*
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun CarbonAndroidTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    testingFontScale: Float = 1f,
    content: @Composable () -> Unit
) {
    /**
     * This will allow you to address the system bars (status bar and navigation bar) without
     * needing to define it within the styles.xml
     *
     * IMPORTANT NOTE:
     * This remember call will persist the system bar changes across all screens. If you need
     * different colors for your system bars in other themes, you will need to override the colors
     * in that theme, as well.
     */
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(
            color = NEUTRON.color
        )
    }

    val activeTypography: Typography = remember(testingFontScale){
        carbonTypography.scale(testingFontScale)
    }

    MaterialTheme(
        colorScheme = if (darkTheme) carbonDarkColorScheme else carbonLightColorScheme,
        typography = activeTypography,
        content = content
    )
}
