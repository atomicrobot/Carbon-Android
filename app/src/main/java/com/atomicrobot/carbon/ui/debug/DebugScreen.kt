package com.atomicrobot.carbon.ui.debug

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DebugScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = 0) {
            // Tab #1  Default
            // Tab #2  Custom Composables
        }
    }
}