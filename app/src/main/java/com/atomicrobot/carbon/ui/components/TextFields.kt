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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.atomicrobot.carbon.R

@Preview
@Composable
fun TransparentTextField(
    modifier: Modifier = Modifier,
    value: String = stringResource(id = R.string.txtField_placeholder),
    labelResId: Int = R.string.label_placeholder,
    onValueChanged: (String) -> Unit = {_ ->}
) {
    TextField(
        value = value,
        onValueChange = onValueChanged,
        label = {
            Text(text = stringResource(id = labelResId))
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent)
    )
}