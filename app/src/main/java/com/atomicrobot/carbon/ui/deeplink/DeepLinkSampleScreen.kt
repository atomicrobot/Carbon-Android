package com.atomicrobot.carbon.ui.deeplink

import android.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.atomicrobot.carbon.navigation.CarbonScreens
import com.atomicrobot.carbon.ui.components.NavigationTopBar
import androidx.compose.ui.graphics.Color as ComposeColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeepLinkSampleScreen(
    modifier: Modifier = Modifier,
    textColor: Int = Color.BLACK,
    textSize: Float = 30f,
    onNavIconClicked: () -> Unit,
) {
    Scaffold(
        topBar = {
            NavigationTopBar(
                title = CarbonScreens.DeepLink.title,
                navigationIcon = Icons.Filled.ArrowBack,
                onNavigationIconClicked = onNavIconClicked
            )
        },
        modifier = modifier,
    ) {
        Surface(
            modifier = Modifier
                .padding(it)
                .background(ComposeColor.White)
        ) {
            Text(
                "Deep Link Sample Fragment",
                fontSize = textSize.sp,
                color = ComposeColor(textColor)
            )
        }
    }
}
