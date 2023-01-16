package com.atomicrobot.carbon.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColorScheme(
    primary = Purple200,
    secondary = Purple700,
    tertiary = pink700,
    onPrimary = Color.White,
    onSurface = Color.White,
//    primaryContainer = ,
//    onPrimaryContainer = ,
//    inversePrimary = ,
//    onSecondary = ,
//    secondaryContainer = ,
//    onSecondaryContainer = ,
//    onTertiary = ,
//    tertiaryContainer = ,
//    onTertiaryContainer = ,
//    background = ,
//    onBackground = ,
//    surface = ,
//    surfaceVariant = ,
//    onSurfaceVariant = ,
//    surfaceTint = ,
//    inverseSurface = ,
//    inverseOnSurface = ,
//    error = ,
//    onError = ,
//    errorContainer = ,
//    onErrorContainer = ,
//    outline = ,
//    outlineVariant = ,
//    scrim =
)

private val LightColorPalette = lightColorScheme(
    primary = Purple500,
    secondary = Purple700,
    tertiary = pink700,
//    onPrimary = Color.White,
//    onSurface = Color.Black,
//    primaryContainer = ,
//    onPrimaryContainer = ,
//    inversePrimary = ,
//    onSecondary = ,
//    secondaryContainer = ,
//    onSecondaryContainer = ,
//    onTertiary = ,
//    tertiaryContainer = ,
//    onTertiaryContainer = ,
//    background = ,
//    onBackground = ,
//    surface = ,
//    surfaceVariant = ,
//    onSurfaceVariant = ,
//    surfaceTint = ,
//    inverseSurface = ,
//    inverseOnSurface = ,
//    error = ,
//    onError = ,
//    errorContainer = ,
//    onErrorContainer = ,
//    outline = ,
//    outlineVariant = ,
//    scrim =

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun CarbonAndroidTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
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
            color = Neutron
        )
    }

    MaterialTheme(
        colorScheme = if (darkTheme) { DarkColorPalette } else { LightColorPalette },
        typography = Typography,
        content = content
    )
}
