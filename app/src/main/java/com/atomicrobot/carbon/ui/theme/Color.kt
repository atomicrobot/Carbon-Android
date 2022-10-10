package com.atomicrobot.carbon.ui.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)
val pink700 = Color(0xFFFF4081)

val Neutron = Color(0xFF101C1D)
val Mono800 = Color(0xFF222C2D)
val CarbonShellProjectItem = Color(0xFF222C2D)

/*
* Lumen Colors
* */
val Black100 = Color(0xFF000000)
val White100 = Color(0xFFFFFFFF)
val White75 = Color(0xBFFFFFFF)
val White50 = Color(0x80FFFFFF)
val White25 = Color(0x40FFFFFF)
val White15 = Color(0x26FFFFFF)
val White3 = Color(0x08FFFFFF)
val LumenBlue = Color(0xBF0E01A7)
val LumenPurple = Color(0xFF04002F)
val DarkBlurple = Color(0xFF05012F)
val LightBlurple = Color(0xFF5A46FF)
val BrightBlurple = Color(0xFF8B83D0)
val MediumBlurple = Color(0xFF4A4867)
val CardBackgroundOn = Color(0x26FFFFFF)
val CardBackgroundOff = Color(0x0DFFFFFF)
val RedPink = Color(0xFFA32758)
val Orange = Color(0xFFE25C33)
val LimeGreen = Color(0xFFC2F13D)
val Teal = Color(0xFF53D8F2)
val Purple = Color(0xFF6F2DFA)
val DeepPink = Color(0xFFC63461)
val YellowOrange = Color(0xFFEF9E37)
val BrightGreen = Color(0xFF60D728)
val LightBlue = Color(0xFF439BDF)
val LightPurple = Color(0xFFB66FEE)
val Pink = Color(0xFFFE54C7)
val Yellow = Color(0xFFFFE661)
val Green = Color(0xFF47C176)
val Blue = Color(0xFF325FFF)
val Magenta = Color(0xFF8F2CBB)
val Red = Color(0xFFEE4134)
val YellowGreen = Color(0xFFEFF847)
val BlueGreen = Color(0xFF3BE1C9)
val DarkBlue = Color(0xFF233CC7)
val Temperature = Color(0xFFFFFBED)
val BlurpleDisabledCTA = Color(0xFF302397)
val BlurpleDisabledText = Color(0xFF828097)

val PresetSwatches by lazy {
    linkedMapOf(
        "RedPink" to RedPink,
        "Orange" to Orange,
        "LimeGreen" to LimeGreen,
        "Teal" to Teal,
        "Purple" to Purple,
        "DeepPink" to DeepPink,
        "YellowOrange" to YellowOrange,
        "BrightGreen" to BrightGreen,
        "LightBlue" to LightBlue,
        "LightPurple" to LightPurple,
        "Pink" to Pink,
        "Yellow" to Yellow,
        "Green" to Green,
        "Blue" to Blue,
        "Magenta" to Magenta,
        "Red" to Red,
        "YellowGreen" to YellowGreen,
        "BlueGreen" to BlueGreen,
        "DarkBlue" to DarkBlue,
    )
}

val BlurpleRadial = Brush.radialGradient(
    colors = listOf(LumenBlue, LumenPurple),
    center = Offset(74.5F, 65.5F),
    radius = 37.25F
)

val CardStrokeLinear = Brush.linearGradient(
    colors = listOf(White50, White3),
    start = Offset(0.0f, 0.0f),
    end = Offset(0.0f, .9897f)
)

val BrightnessHorizontal = Brush.horizontalGradient(
    colors = listOf(Black100, White100)
)

val BrightnessVertical = Brush.verticalGradient(
    colors = listOf(Black100, White100)
)

val BrightnessVerticalUp = Brush.horizontalGradient(
    colors = listOf(White100, Black100)
)

val PinkishHorizontal = Brush.horizontalGradient(
    colors = listOf(Pink, White100)
)

val PinkishVertical = Brush.verticalGradient(
    colors = listOf(Pink, White100)
)

val ColorPickerSweep = Brush.sweepGradient(
    colors = listOf(
        Color(0xFF4BC5FF),
        Color(0xFF6785FF),
        Color(0xFF9470FF),
        Color(0xFFCE61FF),
        Color(0xFFFF59EA),
        Color(0xFFFF54C0),
        Color(0xFFFF4591),
        Color(0xFFFF4B61),
        Color(0xFFFF6F3F),
        Color(0xFFFFAA53),
        Color(0xFFFFD865),
        Color(0xFFFFFD51),
        Color(0xFFD2FF51),
        Color(0xFF8DFF6C),
        Color(0xFF51FFA7),
        Color(0xFF59FFE7),
    )
)

val ColorPickerVertical = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF4BC5FF),
        Color(0xFF6785FF),
        Color(0xFF9470FF),
        Color(0xFFCE61FF),
        Color(0xFFFF59EA),
        Color(0xFFFF54C0),
        Color(0xFFFF4591),
        Color(0xFFFF4B61),
        Color(0xFFFF6F3F),
        Color(0xFFFFAA53),
        Color(0xFFFFD865),
        Color(0xFFFFFD51),
        Color(0xFFD2FF51),
        Color(0xFF8DFF6C),
        Color(0xFF51FFA7),
        Color(0xFF59FFE7),
    )
)
