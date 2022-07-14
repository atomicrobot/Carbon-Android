package com.atomicrobot.carbon.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.atomicrobot.carbon.R
import kotlinx.coroutines.delay
import org.koin.androidx.compose.getViewModel

@Composable
fun SplashScreen(navigate: () -> Unit) {
    val viewModel: SplashViewModel = getViewModel()
    val navigationEvent by viewModel.navigationEvent.collectAsState()

    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "Splash screen",
            contentScale = ContentScale.Inside
        )
    }

    if (navigationEvent == SplashViewModel.ViewNavigation.FirstTime) {
        LaunchedEffect(true) {
            delay(1000) // This is here to show the splash screen for a moment
            navigate()
        }
    } else {
        viewModel.navigateFirstTime()
    }
}
