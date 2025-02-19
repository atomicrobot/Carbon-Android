package com.atomicrobot.carbon.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.atomicrobot.carbon.R

// Set of Material typography styles to start with
val Typography =
    Typography(
        bodyLarge =
            TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
            ),
    )

val Lexend =
    FontFamily(
        Font(R.font.lexend_regular),
        Font(R.font.lexend_bold, FontWeight.Bold),
        Font(R.font.lexend_medium, FontWeight.Medium),
        Font(R.font.lexend_light, FontWeight.Light),
    )

val LumenTypography =
    Typography(
        displayLarge =
            TextStyle(
                fontFamily = Lexend,
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
                lineHeight = 24.sp,
            ),
        displayMedium =
            TextStyle(
                fontFamily = Lexend,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                lineHeight = 24.sp,
            ),
        headlineLarge =
            TextStyle(
                fontFamily = Lexend,
                fontWeight = FontWeight.Light,
                fontSize = 16.sp,
                lineHeight = 24.sp,
            ),
        headlineSmall =
            TextStyle(
                fontFamily = Lexend,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                lineHeight = 16.sp,
            ),
        bodyLarge =
            TextStyle(
                fontFamily = Lexend,
                fontWeight = FontWeight.Light,
                fontSize = 14.sp,
                lineHeight = 16.sp,
            ),
        bodyMedium =
            TextStyle(
                fontFamily = Lexend,
                fontWeight = FontWeight.Light,
                fontSize = 12.sp,
                lineHeight = 16.sp,
            ),
    )

val Typography.ScreenHeading: TextStyle
    get() =
        TextStyle(
            fontFamily = Lexend,
            fontWeight = FontWeight.Bold,
            fontSize = 36.sp,
            lineHeight = 40.sp,
        )
