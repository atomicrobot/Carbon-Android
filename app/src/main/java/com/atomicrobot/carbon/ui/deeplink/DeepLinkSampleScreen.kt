package com.atomicrobot.carbon.ui.deeplink

import android.graphics.Color
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.graphics.Color as ComposeColor
import androidx.compose.ui.unit.sp

@Preview(widthDp = 360, heightDp = 720)
@Composable
fun DeepLinkSampleScreen(textColor: Int = Color.BLACK, textSize: Float = 30f) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            "Deep Link Sample Fragment",
            fontSize = textSize.sp,
            color = ComposeColor(textColor),
            textAlign = TextAlign.Center
        )
    }
}
