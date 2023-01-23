package com.atomicrobot.carbon.ui.designsystem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.atomicrobot.carbon.ui.theme.CarbonAndroidTheme

class DesignSystemActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DesignScreen()
        }
    }
}