package com.atomicrobot.carbon.ui.deeplink

import android.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color as ComposeColor

@Preview(widthDp = 360, heightDp = 720)
@Composable
fun DeepLinkSampleScreen(textColor: Int = Color.BLACK, textSize: Float = 30f) {
    Surface(
        modifier = Modifier.background(ComposeColor.White)
    ) {
        Text(
            "Deep Link Sample Fragment",
            fontSize = textSize.sp,
            color = ComposeColor(textColor)
        )
    }
}
