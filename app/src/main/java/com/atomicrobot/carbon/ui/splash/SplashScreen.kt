package com.atomicrobot.carbon.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.atomicrobot.carbon.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun SplashScreen(navigate: () -> Unit) {
    val viewModel: SplashViewModel = hiltViewModel()

    Column(
        Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "Splash screen",
            contentScale = ContentScale.Inside
        )
    }

    // We only want the event stream to be attached once
    // even if there are multiple re-compositions
    LaunchedEffect(true) {
        viewModel.navigationEvent
            .onEach {
                when (it) {
                    SplashViewModel.ViewNavigation.FirstTime -> {
                        delay(1000) // Briefly show splash screen
                        navigate()
                    }
                    SplashViewModel.ViewNavigation.None -> {
                        viewModel.navigateFirstTime()
                    }
                }
            }.collect()
    }
}
