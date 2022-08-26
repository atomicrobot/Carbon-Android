package com.atomicrobot.carbon

import DesignLumenNavigation
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.view.WindowCompat
import com.atomicrobot.carbon.ui.theme.LightBlurple
import com.atomicrobot.carbon.ui.theme.LumenTheme
import com.atomicrobot.carbon.ui.theme.White100

class DesignLumenActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            LumenTheme {

                val customTextSelectionColors = TextSelectionColors(
                    handleColor = LightBlurple,
                    backgroundColor = LightBlurple.copy(alpha = 0.4f)
                )
                CompositionLocalProvider(
                    LocalContentColor provides White100,
                    LocalTextSelectionColors provides customTextSelectionColors
                ) {
                    DesignLumenNavigation()
                }
            }
        }
    }
}
