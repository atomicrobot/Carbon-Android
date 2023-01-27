package com.atomicrobot.carbon.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.text.TextStyle
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
import kotlin.reflect.KParameter

data class ColorSchemeWrapper(
    val isDarkMode: Boolean,
    val definedColorMap: Map<String,Color>
){

    private val convertedMap: Map<KParameter,Color>
    get() {
        return if (isDarkMode){
            ::darkColorScheme.parameters.filter {
                definedColorMap.containsKey(it.name)
            }.associateWith { definedColorMap[it.name]!! }
        } else {
            ::lightColorScheme.parameters.filter {
                definedColorMap.containsKey(it.name)
            }.associateWith { definedColorMap[it.name]!! }
        }
    }

    fun convertToScheme(): ColorScheme{
        return if (isDarkMode){
            ::darkColorScheme.callBy(convertedMap)
        } else {
            ::lightColorScheme.callBy(convertedMap)
        }
    }
}

val DefaultDarkColorPalette = ColorSchemeWrapper(
    isDarkMode = true,
    definedColorMap = mapOf(
        "primary" to Purple200,
        "secondary" to Purple700,
        "tertiary" to Pink700,
        "onSurface" to MediumGray,
        "primaryContainer" to MediumGray,
        "onPrimaryContainer" to Black100,
        "secondaryContainer" to Orange,
        "onSecondaryContainer" to White100,
        "tertiaryContainer" to Neutron,
        "onTertiaryContainer" to Orange,
        "background" to Black100,
        "onBackground" to White100,
        "surface" to White100,
        "surfaceVariant" to Transparent,
        "onSurfaceVariant" to LightGray,
    )
)

val DefaultLightColorPalette = ColorSchemeWrapper(
    isDarkMode = false,
    definedColorMap = mapOf(
        "primary" to Purple500,
        "secondary" to Purple700,
        "tertiary" to Pink700,
        "onSurface" to MediumGray,
        "primaryContainer" to MediumGray,
        "onPrimaryContainer" to Black100,
        "secondaryContainer" to Orange,
        "onSecondaryContainer" to White100,
        "tertiaryContainer" to Neutron,
        "onTertiaryContainer" to White100,
        "background" to White100,
        "onBackground" to Black100,
        "surface" to Neutron,
        "surfaceVariant" to Transparent,
        "onSurfaceVariant" to LightGray,
    )
)

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
            color = Neutron
        )
    }

    val activeScheme: ColorScheme = remember(paletteTestingMap,darkTheme){
        when (darkTheme){
            true -> DefaultDarkColorPalette.copy(
                definedColorMap = DefaultDarkColorPalette.definedColorMap + paletteTestingMap).convertToScheme()
            else -> DefaultLightColorPalette.copy(
                definedColorMap = DefaultLightColorPalette.definedColorMap + paletteTestingMap).convertToScheme()
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
