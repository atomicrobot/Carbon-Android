package com.atomicrobot.carbon.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

enum class CarbonPalette(val color: Color, val camelCase: String){
    BLACK100(
        color = Color(0xFF000000),
        camelCase = "Black100",
    ),
    LIGHTGRAY(
        color = Color(0xFFCCCCCC),
        camelCase = "LightGray"
    ),
    MEDIUMGRAY(
        color = Color(0xFFE0E0E0),
        camelCase = "MediumGray",
    ),
    NEUTRON(
        color = Color(0xFF101C1D),
        camelCase = "Neutron",
    ),
    ORANGE(
        color = Color(0xFFE25C33),
        camelCase = "Orange",
    ),
    PINK700(
        color = Color(0xFFFF4081),
        camelCase = "Pink700",
    ),
    PURPLE200(
        color = Color(0xFFBB86FC),
        camelCase = "Purple200",
    ),
    PURPLE500(
        color = Color(0xFF6200EE),
        camelCase = "Purple500",
    ),
    PURPLE700(
        color = Color(0xFF3700B3),
        camelCase = "Purple700",
    ),
    TRANSPARENT(
        color = Color(0x00000000),
        camelCase = "Transparent",
    ),
    WHITE100(
        color = Color(0xFFFFFFFF),
        camelCase = "White100",
    )
}


/**
 * Dark color mappings
 * Any values added, removed or changed in the color scheme should also be adjusted in the applied
 * and unapplied maps. These are necessary for displaying theme values on Design System screens. The applied
 * map should contain all custom colors supplied to the scheme, while the unapplied map should contain
 * all default colors used.
 */
val appliedDarkColorMap = mapOf(
    "primary" to CarbonPalette.PURPLE200,
    "secondary" to CarbonPalette.PURPLE700,
    "tertiary" to CarbonPalette.PINK700,
    "onSurface" to CarbonPalette.MEDIUMGRAY,
    "primaryContainer" to CarbonPalette.MEDIUMGRAY,
    "onPrimaryContainer" to CarbonPalette.BLACK100,
    "secondaryContainer" to CarbonPalette.ORANGE,
    "onSecondaryContainer" to CarbonPalette.WHITE100,
    "tertiaryContainer" to CarbonPalette.NEUTRON,
    "onTertiaryContainer" to CarbonPalette.ORANGE,
    "background" to CarbonPalette.BLACK100,
    "onBackground" to CarbonPalette.WHITE100,
    "surface" to CarbonPalette.WHITE100,
    "surfaceVariant" to CarbonPalette.TRANSPARENT,
    "onSurfaceVariant" to CarbonPalette.LIGHTGRAY
)

val carbonDarkColorScheme: ColorScheme = darkColorScheme(
    primary = CarbonPalette.PURPLE200.color,
    secondary = CarbonPalette.PURPLE700.color,
    tertiary = CarbonPalette.PINK700.color,
    onSurface = CarbonPalette.MEDIUMGRAY.color,
    primaryContainer = CarbonPalette.MEDIUMGRAY.color,
    onPrimaryContainer = CarbonPalette.BLACK100.color,
    secondaryContainer = CarbonPalette.ORANGE.color,
    onSecondaryContainer = CarbonPalette.WHITE100.color,
    tertiaryContainer = CarbonPalette.NEUTRON.color,
    onTertiaryContainer = CarbonPalette.ORANGE.color,
    background = CarbonPalette.BLACK100.color,
    onBackground = CarbonPalette.WHITE100.color,
    surface = CarbonPalette.WHITE100.color,
    surfaceVariant = CarbonPalette.TRANSPARENT.color,
    onSurfaceVariant = CarbonPalette.LIGHTGRAY.color
)

val unappliedDarkColorMap = mapOf(
    "onPrimary" to carbonDarkColorScheme.onPrimary,
    "inversePrimary" to carbonDarkColorScheme.inversePrimary,
    "onSecondary" to carbonDarkColorScheme.onSecondary,
    "onTertiary" to carbonDarkColorScheme.onTertiary,
    "surfaceTint" to carbonDarkColorScheme.surfaceTint,
    "inverseSurface" to carbonDarkColorScheme.inverseSurface,
    "inverseOnSurface" to carbonDarkColorScheme.inverseOnSurface,
    "error" to carbonDarkColorScheme.error,
    "onError" to carbonDarkColorScheme.onError,
    "errorContainer" to carbonDarkColorScheme.errorContainer,
    "onErrorContainer" to carbonDarkColorScheme.onErrorContainer,
    "outline" to carbonDarkColorScheme.outline,
    "outlineVariant" to carbonDarkColorScheme.outlineVariant,
    "scrim" to carbonDarkColorScheme.scrim
)


/**
 * Light color mappings
 * Any values added, removed or changed in the color scheme should also be adjusted in the applied
 * and unapplied maps. These are necessary for displaying theme values on Design System screens. The applied
 * map should contain all custom colors supplied to the scheme, while the unapplied map should contain
 * all default colors used.
 */
val appliedLightColorMap = mapOf(
    "primary" to CarbonPalette.PURPLE500,
    "secondary" to CarbonPalette.PURPLE700,
    "tertiary" to CarbonPalette.PINK700,
    "onSurface" to CarbonPalette.MEDIUMGRAY,
    "primaryContainer" to CarbonPalette.MEDIUMGRAY,
    "onPrimaryContainer" to CarbonPalette.BLACK100,
    "secondaryContainer" to CarbonPalette.ORANGE,
    "onSecondaryContainer" to CarbonPalette.WHITE100,
    "tertiaryContainer" to CarbonPalette.NEUTRON,
    "onTertiaryContainer" to CarbonPalette.WHITE100,
    "background" to CarbonPalette.WHITE100,
    "onBackground" to CarbonPalette.BLACK100,
    "surface" to CarbonPalette.NEUTRON,
    "surfaceVariant" to CarbonPalette.TRANSPARENT,
    "onSurfaceVariant" to CarbonPalette.LIGHTGRAY
)

val carbonLightColorScheme: ColorScheme = lightColorScheme(
    primary = CarbonPalette.PURPLE500.color,
    secondary = CarbonPalette.PURPLE700.color,
    tertiary = CarbonPalette.PINK700.color,
    onSurface = CarbonPalette.MEDIUMGRAY.color,
    primaryContainer = CarbonPalette.MEDIUMGRAY.color,
    onPrimaryContainer = CarbonPalette.BLACK100.color,
    secondaryContainer = CarbonPalette.ORANGE.color,
    onSecondaryContainer = CarbonPalette.WHITE100.color,
    tertiaryContainer = CarbonPalette.NEUTRON.color,
    onTertiaryContainer = CarbonPalette.WHITE100.color,
    background = CarbonPalette.WHITE100.color,
    onBackground = CarbonPalette.BLACK100.color,
    surface = CarbonPalette.NEUTRON.color,
    surfaceVariant = CarbonPalette.TRANSPARENT.color,
    onSurfaceVariant = CarbonPalette.LIGHTGRAY.color
)

val unappliedLightColorMap = mapOf(
    "onPrimary" to carbonLightColorScheme.onPrimary,
    "inversePrimary" to carbonLightColorScheme.inversePrimary,
    "onSecondary" to carbonLightColorScheme.onSecondary,
    "onTertiary" to carbonLightColorScheme.onTertiary,
    "surfaceTint" to carbonLightColorScheme.surfaceTint,
    "inverseSurface" to carbonLightColorScheme.inverseSurface,
    "inverseOnSurface" to carbonLightColorScheme.inverseOnSurface,
    "error" to carbonLightColorScheme.error,
    "onError" to carbonLightColorScheme.onError,
    "errorContainer" to carbonLightColorScheme.errorContainer,
    "onErrorContainer" to carbonLightColorScheme.onErrorContainer,
    "outline" to carbonLightColorScheme.outline,
    "outlineVariant" to carbonLightColorScheme.outlineVariant,
    "scrim" to carbonLightColorScheme.scrim
)
