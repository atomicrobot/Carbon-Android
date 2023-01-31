package com.atomicrobot.carbon.util

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController

//region Activity extensions
@Suppress("unused")
inline fun <reified T : ComponentActivity> ComponentActivity.startComponentActivity() {
    startActivity(Intent(this, T::class.java))
}
//endregion

//region Helper functions
fun splitCamelCase(s: String): String {
    return s.replace(
        String.format(
            "%s|%s|%s",
            "(?<=[A-Z])(?=[A-Z][a-z])",
            "(?<=[^A-Z])(?=[A-Z])",
            "(?<=[A-Za-z])(?=[^A-Za-z])"
        ).toRegex(),
        "/"
    )
}
//endregion

//region Typography extension
fun Typography.scale(scaleFactor: Float): Typography {
    return this.copy(
        displayLarge = this.displayLarge.copy(
            fontSize = this.displayLarge.fontSize.times(scaleFactor)
        ),
        displayMedium = this.displayMedium.copy(
            fontSize = this.displayMedium.fontSize.times(scaleFactor)
        ),
        displaySmall = this.displaySmall.copy(
            fontSize = this.displaySmall.fontSize.times(scaleFactor)
        ),
        headlineLarge = this.headlineLarge.copy(
            fontSize = this.headlineLarge.fontSize.times(scaleFactor)
        ),
        headlineMedium = this.headlineMedium
            .copy(
                fontSize = this.headlineMedium.fontSize.times(scaleFactor)
            ),
        headlineSmall = this.headlineSmall.copy(
            fontSize = this.headlineSmall.fontSize.times(scaleFactor)
        ),
        titleLarge = this.titleLarge.copy(
            fontSize = this.titleLarge.fontSize.times(scaleFactor)
        ),
        titleMedium = this.titleMedium.copy(
            fontSize = this.titleMedium.fontSize.times(scaleFactor)
        ),
        titleSmall = this.titleSmall.copy(
            fontSize = this.titleSmall.fontSize.times(scaleFactor)
        ),
        bodyLarge = this.bodyLarge.copy(
            fontSize = this.bodyLarge.fontSize.times(scaleFactor)
        ),
        bodyMedium = this.bodyMedium.copy(
            fontSize = this.bodyMedium.fontSize.times(scaleFactor)
        ),
        bodySmall = this.bodySmall.copy(
            fontSize = this.bodySmall.fontSize.times(scaleFactor)
        ),
        labelLarge = this.labelLarge.copy(
            fontSize = this.labelLarge.fontSize.times(scaleFactor)
        ),
        labelMedium = this.labelMedium.copy(
            fontSize = this.labelMedium.fontSize.times(scaleFactor)
        ),
        labelSmall = this.labelSmall.copy(
            fontSize = this.labelSmall.fontSize.times(scaleFactor)
        ),
    )
}
//endregion

@Composable
fun NavBackStackEntry.rememberParentEntry(navController: NavController): NavBackStackEntry {
    // First, get the parent of the current destination
    // This always exists since every destination in your graph has a parent
    val parentId = destination.parent!!.id

    // Now get the NavBackStackEntry associated with the parent
    // making sure to remember it
    return remember(this) {
        navController.getBackStackEntry(parentId)
    }
}


@Composable
inline fun <reified VM : ViewModel> NavBackStackEntry.getNavigationScopedViewModel(
    navController: NavController
): VM {
    val parentEntry = rememberParentEntry(navController)
    return viewModel(parentEntry)
}

