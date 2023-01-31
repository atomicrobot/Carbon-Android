package com.atomicrobot.carbon.ui.designsystem.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

@Suppress("unused")
object FinAllyColors {
//region Greys
    val grey1000: Color = Color(0xFF000906)
    val grey950: Color = Color(0xFF151918)
    val grey900: Color = Color(0xFF1C2220)
    val grey850: Color = Color(0xFF222A28)
    val grey800: Color = Color(0xFF293230)
    val grey750: Color = Color(0xFF303B38)
    val grey700: Color = Color(0xFF3E4C48)
    val grey650: Color = Color(0xFF4C5D58)
    val grey600: Color = Color(0xFF5A6D69)
    val grey550: Color = Color(0xFF677E79)
    val grey500: Color = Color(0xFF768F88)
    val grey450: Color = Color(0xFF8FA39E)
    val grey400: Color = Color(0xFFA5B6B2)
    val grey350: Color = Color(0xFFBCC8C5)
    val grey300: Color = Color(0xFFC7D1CF)
    val grey250: Color = Color(0xFFD2DAD8)
    val grey200: Color = Color(0xFFDDE3E2)
    val grey150: Color = Color(0xFFE9EDEC)
    val grey100: Color = Color(0xFFF4F6F5)
    val grey50: Color = Color(0xFFF8F9F9)
    val grey0: Color = Color(0xFFFFFFFF)
//endregion

//region Green
    val green900: Color = Color(0xFF086756)
    val green800: Color = Color(0xFF098670)
    val green700: Color = Color(0xFF48AA92)
    val green600: Color = Color(0xFF59B69F)
    val green500: Color = Color(0xFF71BEAB)
    val green400: Color = Color(0xFF8CC8BA)
    val green300: Color = Color(0xFFA8D5CA)
    val green200: Color = Color(0xFFC7E3DC)
    val green100: Color = Color(0xFFEAF4F1)
//endregion

//region Red
    val red400: Color = Color(0xFF670808)
    val red300: Color = Color(0xFF860909)
    val red200: Color = Color(0xFFAA4848)
    val red100: Color = Color(0xFFD4A3A3)
//endregion

    val colorMap: Map<String, Int> =
        mapOf(
            // Grey
            "Grey 1000" to grey1000.toArgb(),
            "Grey 950" to grey950.toArgb(),
            "Grey 900" to grey900.toArgb(),
            "Grey 850" to grey850.toArgb(),
            "Grey 800" to grey800.toArgb(),
            "Grey 750" to grey750.toArgb(),
            "Grey 700" to grey700.toArgb(),
            "Grey 650" to grey650.toArgb(),
            "Grey 600" to grey600.toArgb(),
            "Grey 550" to grey550.toArgb(),
            "Grey 500" to grey500.toArgb(),
            "Grey 450" to grey450.toArgb(),
            "Grey 400" to grey400.toArgb(),
            "Grey 350" to grey350.toArgb(),
            "Grey 300" to grey300.toArgb(),
            "Grey 250" to grey250.toArgb(),
            "Grey 200" to grey200.toArgb(),
            "Grey 150" to grey150.toArgb(),
            "Grey 100" to grey100.toArgb(),
            "Grey 50" to grey50.toArgb(),
            "Grey 0" to grey0.toArgb(),
            // Green
            "Green 900" to green900.toArgb(),
            "Green 800" to green800.toArgb(),
            "Green 700" to green700.toArgb(),
            "Green 600" to green600.toArgb(),
            "Green 500" to green500.toArgb(),
            "Green 400" to green400.toArgb(),
            "Green 300" to green300.toArgb(),
            "Green 200" to green200.toArgb(),
            "Green 100" to green100.toArgb(),
            // Red
            "Red 400" to red400.toArgb(),
            "Red 300" to red300.toArgb(),
            "Red 200" to red200.toArgb(),
            "Red 100" to red100.toArgb(),
        )
}
