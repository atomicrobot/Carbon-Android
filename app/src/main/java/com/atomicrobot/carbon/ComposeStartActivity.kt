package com.atomicrobot.carbon

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Text
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.atomicrobot.carbon.ui.ComposeBaseActivity
import com.atomicrobot.carbon.ui.main.Main
import com.atomicrobot.carbon.ui.main.MainContent
import com.atomicrobot.carbon.ui.main.MainViewModelCompose
import com.atomicrobot.carbon.ui.theme.CarbonAndroidTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ComposeStartActivity : ComposeBaseActivity() {

    private val viewModelCompose: MainViewModelCompose by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            CarbonAndroidTheme {
                Main(viewModelCompose)
            }
        }
    }
}