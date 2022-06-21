package com.atomicrobot.carbon.ui.deeplink

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp


@Composable
fun DeepLinkPath1(deepLinkSampleViewModel: DeepLinkSampleViewModel) {
    val color = deepLinkSampleViewModel.deepLinkTextColor
    val textSize = deepLinkSampleViewModel.deepLinkTextSize.sp
    Greeting(deepLinkText = "Deep Link Sample Compose", color, textSize )
}


@Composable
private fun Greeting(deepLinkText: String, color: Int, fontSize: TextUnit)
{
    Column(modifier = Modifier.fillMaxWidth().wrapContentSize(align = Alignment.Center)) {
        Text(text = deepLinkText, color = Color(color), fontSize = fontSize, textAlign = TextAlign.Center)
    }

}


//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview(){
//    CarbonAndroidTheme() {
//        Greeting("Deep Link Sample Fragment")
//    }
//}
