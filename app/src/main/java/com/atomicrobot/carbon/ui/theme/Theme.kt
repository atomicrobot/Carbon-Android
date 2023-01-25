package com.atomicrobot.carbon.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import com.atomicrobot.carbon.util.scale
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val darkColorScheme = darkColorScheme(
    primary = Purple200,
    secondary = Purple700,
    tertiary = pink700,
)

private val lightColorScheme = lightColorScheme(
    primary = Purple500,
    secondary = Purple700,
    tertiary = pink700,
)

@Composable
fun CarbonAndroidTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    fontScale: Float = 1.0f,
    useDynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val dynamicColor = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) && useDynamicColor
    val ctx = LocalContext.current
    val colorScheme = when {
        dynamicColor && darkTheme -> dynamicDarkColorScheme(ctx)
        dynamicColor && !darkTheme -> dynamicLightColorScheme(ctx)
        darkTheme -> darkColorScheme
        else -> lightColorScheme
    }

    /**
     * This will allow you to address the system bars (status bar and navigation bar) without
     * needing to define it within the styles.xml
     *
     * IMPORTANT NOTE:
     * This remember call will persist the system bar changes across all screens. If you need
     * different colors for your system bars in other themes, you will need to override the colors
     * in that theme, as well.
     */
    val view = LocalView.current
    if (!view.isInEditMode) {
        val systemUiController = rememberSystemUiController()
        SideEffect {
            systemUiController.setSystemBarsColor(
                color = Neutron
            )
        }
    }

    val typography = if (fontScale == 1.0f) carbonTypography
    else carbonTypography.scale(scaleFactor = fontScale)

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = content
    )
}
