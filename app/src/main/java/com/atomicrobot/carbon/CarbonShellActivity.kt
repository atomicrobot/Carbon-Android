package com.atomicrobot.carbon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import com.atomicrobot.carbon.ui.shell.CarbonShellNavigation
import com.atomicrobot.carbon.ui.theme.CarbonShellTheme
import com.atomicrobot.carbon.util.LocalActivity

/**
 * Activity housing the multi-app selector
 */
class CarbonShellActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CarbonShellTheme {
                // Provide activity context so CarbonShellActivity can call activity.finish()
                CompositionLocalProvider(LocalActivity provides this) {
                    CarbonShellNavigation()
                }
            }
        }
    }
}
