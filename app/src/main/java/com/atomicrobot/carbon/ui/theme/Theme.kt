package com.atomicrobot.carbon.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = pink700,
    onPrimary = Color.White,
    onSurface = Color.White
)

private val LightColorPalette = lightColors(
    primary = Purple500,
    primaryVariant = Purple700,
    secondary = pink700

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
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        content = content
    )
}

private val LumenColorPalette = lightColors(
    primary = DarkBlurple,
    onPrimary = White100,
    surface = DarkBlurple,
    onSurface = White100
)

@Composable
fun LumenTheme(
    @Suppress("UNUSED_PARAMETER") darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = LumenColorPalette,
        typography = LumenTypography,
        shapes = carbonShapes,
        content = content
    )
}

private val CarbonShellPalette = lightColors(
    primary = Neutron,
    onPrimary = White100,
    surface = Mono800,
    onSurface = White100
)

@Composable
fun CarbonShellTheme(
    @Suppress("UNUSED_PARAMETER") darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = CarbonShellPalette,
        shapes = carbonShellShapes,
        content = content
    )
}