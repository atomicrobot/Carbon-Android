package com.atomicrobot.carbon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.atomicrobot.carbon.ui.main.MainScreen
import com.atomicrobot.carbon.ui.main.MainViewModelCompose
import com.atomicrobot.carbon.ui.theme.CarbonAndroidTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class ComposeStartActivity : ComponentActivity() {

    private val viewModel: MainViewModelCompose by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            CarbonAndroidTheme {
                MainScreen(viewModel)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.saveState(outState)
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }
}