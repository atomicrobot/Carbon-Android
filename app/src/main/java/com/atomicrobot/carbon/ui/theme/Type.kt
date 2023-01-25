package com.atomicrobot.carbon.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import kotlin.reflect.KParameter
import kotlin.reflect.full.memberProperties


data class TypographyWrapper(
    val typography: Typography,
    val fontScale: Float,
){

    fun scaledTypography(): Typography {
        return ::Typography.callBy(
            ::Typography.parameters.associateWith { generic ->
                val associatedTextStyle = (typography::class.memberProperties.first { local ->
                    local.name == generic.name
                }.getter.call(typography) as TextStyle)

                associatedTextStyle.copy(
                    fontSize = associatedTextStyle.fontSize.times(fontScale)
                )
            }
        )
    }
}

// Set of Material typography styles to start with
val DefaultTypography = TypographyWrapper(
    typography = Typography(),
    fontScale = 1f
//    displayLarge = ,
//    displayMedium = ,
//    displaySmall = ,
//    headlineLarge = ,
//    headlineMedium = ,
//    headlineSmall = ,
//    titleLarge = ,
//    titleMedium = ,
//    titleSmall = ,
//    bodyLarge = ,
//    bodyMedium = ,
//    bodySmall = ,
//    labelLarge = ,
//    labelMedium = ,
//    labelSmall =


        
    /*body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )

    Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)
