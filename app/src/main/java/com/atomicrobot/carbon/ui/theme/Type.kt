package com.atomicrobot.carbon.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle


/**
 * Typography mappings
 * Any values added, removed or changed in carbonTopography should also be adjusted in the applied
 * and unapplied maps. These are necessary for displaying theme values on Design System screens. The applied
 * map should contain all custom styles supplied to the Typography, while the unapplied map should contain
 * all default styles used.
 */
val carbonTypography = Typography()

val appliedTypographyMap = mapOf<String,TextStyle>()

val unappliedTypographyMap = mapOf(
    "displaylarge" to carbonTypography.displayLarge,
    "displayMedium" to carbonTypography.displayMedium,
    "displaySmall" to carbonTypography.displaySmall,
    "headlingLarge" to carbonTypography.headlineLarge,
    "headlineMedium" to carbonTypography.headlineMedium,
    "headlineSmall" to carbonTypography.headlineSmall,
    "titleLarge" to carbonTypography.titleLarge,
    "titleMedium" to carbonTypography.titleMedium,
    "titleSmall" to carbonTypography.titleSmall,
    "bodyLarge" to carbonTypography.bodyLarge,
    "bodyMedium" to carbonTypography.bodyMedium,
    "bodySmall" to carbonTypography.bodySmall,
    "labelLarge" to carbonTypography.labelLarge,
    "labelMedium" to carbonTypography.labelMedium,
    "labelSmall" to carbonTypography.labelSmall
)

fun Typography.scale(fontScale: Float): Typography {
    return this.copy(
        displayLarge = this.displayLarge.copy(
            fontSize = this.displayLarge.fontSize.times(fontScale)
        ),
        displayMedium = this.displayMedium.copy(
            fontSize = this.displayMedium.fontSize.times(fontScale)
        ),
        displaySmall = this.displaySmall.copy(
            fontSize = this.displaySmall.fontSize.times(fontScale)
        ),
        headlineLarge = this.headlineLarge.copy(
            fontSize = this.headlineLarge.fontSize.times(fontScale)
        ),
        headlineMedium = this.headlineMedium.copy(
            fontSize = this.headlineMedium.fontSize.times(fontScale)
        ),
        headlineSmall = this.headlineSmall.copy(
            fontSize = this.headlineSmall.fontSize.times(fontScale)
        ),
        titleLarge = this.titleLarge.copy(
            fontSize = this.titleLarge.fontSize.times(fontScale)
        ),
        titleMedium = this.titleMedium.copy(
            fontSize = this.titleMedium.fontSize.times(fontScale)
        ),
        titleSmall = this.titleSmall.copy(
            fontSize = this.titleSmall.fontSize.times(fontScale)
        ),
        bodyLarge = this.bodyLarge.copy(
            fontSize = this.bodyLarge.fontSize.times(fontScale)
        ),
        bodyMedium = this.bodyMedium.copy(
            fontSize = this.bodyMedium.fontSize.times(fontScale)
        ),
        bodySmall = this.bodySmall.copy(
            fontSize = this.bodySmall.fontSize.times(fontScale)
        ),
        labelLarge = this.labelLarge.copy(
            fontSize = this.labelLarge.fontSize.times(fontScale)
        ),
        labelMedium = this.labelMedium.copy(
            fontSize = this.labelMedium.fontSize.times(fontScale)
        ),
        labelSmall = this.labelSmall.copy(
            fontSize = this.labelSmall.fontSize.times(fontScale)
        ),
    )
}
