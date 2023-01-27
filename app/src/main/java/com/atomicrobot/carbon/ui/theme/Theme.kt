package com.atomicrobot.carbon.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.atomicrobot.carbon.ui.theme.CarbonPalette.*
import com.google.accompanist.systemuicontroller.rememberSystemUiController

fun Map<String,Color>.convertToScheme(isDarkMode: Boolean): ColorScheme{
    val convertedMap = if (isDarkMode){
        ::darkColorScheme.parameters.filter {
            this.containsKey(it.name)
        }.associateWith { this[it.name]!! }
    } else {
        ::lightColorScheme.parameters.filter {
            this.containsKey(it.name)
        }.associateWith { this[it.name]!! }
    }

    return if (isDarkMode){
        ::darkColorScheme.callBy(convertedMap)
    } else {
        ::lightColorScheme.callBy(convertedMap)
    }
}

@Composable
fun CarbonAndroidTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    paletteTestingMap: Map<String,Color> = emptyMap(),
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

    val activeScheme: ColorScheme = remember(paletteTestingMap,darkTheme){
        when (darkTheme){
            true -> (defaultDarkColorMap + paletteTestingMap).convertToScheme(true)
            else -> (defaultLightColorMap + paletteTestingMap).convertToScheme(false)
        }
    }

    val activeTypography: Typography = remember(testingFontScale){
        DefaultTypography.copy(
            fontScale = testingFontScale
        ).scaledTypography()
    }

    MaterialTheme(
        colorScheme = activeScheme,
        typography = activeTypography,
        content = content
    )
}
