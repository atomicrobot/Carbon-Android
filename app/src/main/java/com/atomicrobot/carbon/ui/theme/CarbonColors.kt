package com.atomicrobot.carbon.ui.theme

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

val defaultDarkColorMap = mapOf(
    "primary" to CarbonPalette.PURPLE200.color,
    "secondary" to CarbonPalette.PURPLE700.color,
    "tertiary" to CarbonPalette.PINK700.color,
    "onSurface" to CarbonPalette.MEDIUMGRAY.color,
    "primaryContainer" to CarbonPalette.MEDIUMGRAY.color,
    "onPrimaryContainer" to CarbonPalette.BLACK100.color,
    "secondaryContainer" to CarbonPalette.ORANGE.color,
    "onSecondaryContainer" to CarbonPalette.WHITE100.color,
    "tertiaryContainer" to CarbonPalette.NEUTRON.color,
    "onTertiaryContainer" to CarbonPalette.ORANGE.color,
    "background" to CarbonPalette.BLACK100.color,
    "onBackground" to CarbonPalette.WHITE100.color,
    "surface" to CarbonPalette.WHITE100.color,
    "surfaceVariant" to CarbonPalette.TRANSPARENT.color,
    "onSurfaceVariant" to CarbonPalette.LIGHTGRAY.color
)

val defaultLightColorMap = mapOf(
    "primary" to CarbonPalette.PURPLE500.color,
    "secondary" to CarbonPalette.PURPLE700.color,
    "tertiary" to CarbonPalette.PINK700.color,
    "onSurface" to CarbonPalette.MEDIUMGRAY.color,
    "primaryContainer" to CarbonPalette.MEDIUMGRAY.color,
    "onPrimaryContainer" to CarbonPalette.BLACK100.color,
    "secondaryContainer" to CarbonPalette.ORANGE.color,
    "onSecondaryContainer" to CarbonPalette.WHITE100.color,
    "tertiaryContainer" to CarbonPalette.NEUTRON.color,
    "onTertiaryContainer" to CarbonPalette.WHITE100.color,
    "background" to CarbonPalette.WHITE100.color,
    "onBackground" to CarbonPalette.BLACK100.color,
    "surface" to CarbonPalette.NEUTRON.color,
    "surfaceVariant" to CarbonPalette.TRANSPARENT.color,
    "onSurfaceVariant" to CarbonPalette.LIGHTGRAY.color
)
