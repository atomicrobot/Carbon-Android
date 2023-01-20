package com.atomicrobot.carbon.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import com.atomicrobot.carbon.ui.theme.CarbonColors.Companion.Black100
import com.atomicrobot.carbon.ui.theme.CarbonColors.Companion.MediumGray
import com.atomicrobot.carbon.ui.theme.CarbonColors.Companion.Neutron
import com.atomicrobot.carbon.ui.theme.CarbonColors.Companion.Orange
import com.atomicrobot.carbon.ui.theme.CarbonColors.Companion.Pink700
import com.atomicrobot.carbon.ui.theme.CarbonColors.Companion.Purple200
import com.atomicrobot.carbon.ui.theme.CarbonColors.Companion.Purple500
import com.atomicrobot.carbon.ui.theme.CarbonColors.Companion.Purple700
import com.atomicrobot.carbon.ui.theme.CarbonColors.Companion.Transparent
import com.atomicrobot.carbon.ui.theme.CarbonColors.Companion.White100
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColorScheme(
    primary = Purple200,
    secondary = Purple700,
    tertiary = Pink700,
    onPrimary = White100,
    onSurface = MediumGray,
    primaryContainer = MediumGray,
    onPrimaryContainer = Black100,
//    inversePrimary = ,
//    onSecondary = ,
    secondaryContainer = Orange,
    onSecondaryContainer = White100,
//    onTertiary = ,
    tertiaryContainer = Neutron,
    onTertiaryContainer = White100,
    background = Black100,
    onBackground = White100,
    surface = Neutron,
    surfaceVariant = Transparent,
    onSurfaceVariant = LightGray,
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
    tertiary = Pink700,
//    onPrimary = Color.White,
    onSurface = MediumGray,
    primaryContainer = MediumGray,
    onPrimaryContainer = Black100,
//    inversePrimary = ,
//    onSecondary = ,
    secondaryContainer = Orange,
    onSecondaryContainer = White100,
//    onTertiary = ,
    tertiaryContainer = Neutron,
    onTertiaryContainer = White100,
    background = White100,
    onBackground = Black100,
    surface = Neutron,
    surfaceVariant = Transparent,
    onSurfaceVariant = LightGray,
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
