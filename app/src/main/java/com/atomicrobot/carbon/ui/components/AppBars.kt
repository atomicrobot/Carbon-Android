package com.atomicrobot.carbon.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.atomicrobot.carbon.BuildConfig
import com.atomicrobot.carbon.R

@Preview
@Composable
fun TopBar(modifier: Modifier = Modifier) {
    TopAppBar(
        modifier = modifier,
        title = { Text(text = BuildConfig.APPLICATION_ID) }
    )
}

@Preview
@Composable
fun BottomBar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colors.onSurface
                    .copy(alpha = TextFieldDefaults.BackgroundOpacity)
            )
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp)
    ) {
        Text(
            text = stringResource(
                id = R.string.version_format, BuildConfig.VERSION_NAME
            ),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = stringResource(
                id = R.string.fingerprint_format, BuildConfig.VERSION_FINGERPRINT
            )
        )
    }
}