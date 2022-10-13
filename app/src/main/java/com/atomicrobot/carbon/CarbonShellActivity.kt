package com.atomicrobot.carbon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.atomicrobot.carbon.ui.shell.CarbonShellNavigation
import com.atomicrobot.carbon.ui.theme.CarbonShellTheme

/**
 * Activity housing the multi-app selector
 */
class CarbonShellActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CarbonShellTheme {
                CarbonShellNavigation()
            }
        }
    }
}
