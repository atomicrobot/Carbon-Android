package com.atomicrobot.carbon.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette =
    darkColorScheme(
        primary = Purple200,
        secondary = Purple700,
        tertiary = pink700,
        onPrimary = Color.White,
        onSurface = Color.White,
    )

private val LightColorPalette =
    lightColorScheme(
        primary = Purple500,
        secondary = Purple700,
        tertiary = pink700,
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
    content: @Composable () -> Unit,
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
            color = Neutron,
        )
    }

    MaterialTheme(
        colorScheme =
            if (darkTheme) {
                DarkColorPalette
            } else {
                LightColorPalette
            },
        typography = Typography,
        content = content,
    )
}

private val CarbonShellPalette =
    lightColorScheme(
        primary = Neutron,
        onPrimary = White100,
        surface = Mono800,
        onSurface = White100,
    )

@Composable
fun CarbonShellTheme(
    @Suppress("UNUSED_PARAMETER") darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(
            color = Neutron,
        )
    }

    MaterialTheme(
        colorScheme = CarbonShellPalette,
        shapes = carbonShellShapes,
        content = content,
    )
}

private val LumenColorPalette =
    lightColorScheme(
        primary = DarkBlurple,
        onPrimary = White100,
        surface = DarkBlurple,
        onSurface = White100,
    )

@Composable
fun LumenTheme(
    @Suppress("UNUSED_PARAMETER") darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(
            color = Neutron,
        )
    }

    MaterialTheme(
        colorScheme = LumenColorPalette,
        typography = LumenTypography,
        shapes = carbonShapes,
        content = content,
    )
}

private val ScannerColorPalette =
    lightColorScheme(
        primary = Neutron,
        onPrimary = White100,
        surface = Mono800,
        onSurface = White100,
    )

@Composable
fun ScannerTheme(
    @Suppress("UNUSED_PARAMETER") darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(
            color = Neutron,
        )
    }

    MaterialTheme(
        colorScheme = ScannerColorPalette,
        typography = Typography,
        content = content,
    )
}
