package com.atomicrobot.carbon

import DesignLumenNavigation
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.atomicrobot.carbon.ui.theme.LumenTheme

class DesignLumenActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            LumenTheme {
                DesignLumenNavigation()
            }
        }
    }
}
