package com.atomicrobot.carbon.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.ui.theme.Purple700
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navigate: () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .background(if (isSystemInDarkTheme()) Purple700 else Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "Splash screen",
            contentScale = ContentScale.Inside
        )
    }
    LaunchedEffect(key1 = true) {
        delay(1000)
        navigate()
    }
}
