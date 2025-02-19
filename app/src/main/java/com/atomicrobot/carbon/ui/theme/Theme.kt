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
        secondaryContainer = Purple200,
        onSurfaceVariant = Mono800,
        surfaceContainer = Purple200,
    )

private val LightColorPalette =
    lightColorScheme(
        primary = Purple500,
        secondary = Purple700,
        secondaryContainer = Purple500,
        onSecondaryContainer = White100,
        background = White100,
        onBackground = Black100,
        onSurface = Black100,
        onSurfaceVariant = White75,
        surfaceContainer = Purple500,
        surfaceContainerHighest = White100,
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
     *
     * UPDATE:
     * With API 35, UI will now draw edge-to-edge by default. If using Material3 and minSdk is set
     * to 35, then you can completely remove systemUiController logic.
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
        secondaryContainer = DarkBlurple,
        onSurfaceVariant = Color.Transparent,
        surfaceContainer = DarkBlurple,
        surfaceContainerHighest = Color.Transparent,
        surfaceContainerLow = LumenPurple,
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
