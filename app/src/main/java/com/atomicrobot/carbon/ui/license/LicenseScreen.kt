package com.atomicrobot.carbon.ui.license

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.atomicrobot.carbon.ui.theme.CarbonAndroidTheme
import java.io.InputStream

@Composable
fun LicenseScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ReadDataFile()
    }
}

@Preview(showBackground = true)
@Composable
fun LicenseScreenPreview() {
    CarbonAndroidTheme {
        LicenseScreen()
    }
}

@Preview
@Composable
fun ReadDataFile() {
    var dataText by remember { mutableStateOf("") }
    LazyColumn {
        item {
            Text(dataText)
        }
    }
    val context = LocalContext.current
    LaunchedEffect(true) {
        kotlin.runCatching {
            val inputStream: InputStream = context.assets.open("licenses.txt")
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            String(buffer)
        }.onSuccess {
            dataText = it
        }.onFailure {
            dataText = "error"
        }

    }
}