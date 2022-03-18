package com.atomicrobot.carbon.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun TransparentTextField(
    value: String,
    labelResId: Int,
    modifier: Modifier = Modifier,
    onValueChanged: (String) -> Unit
) {
    TextField(
        value = value,
        onValueChange = onValueChanged,
        label = {
            Text(text = stringResource(id = labelResId))
        },
        modifier = modifier.fillMaxWidth().padding(bottom = 8.dp),
        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent)
    )
}