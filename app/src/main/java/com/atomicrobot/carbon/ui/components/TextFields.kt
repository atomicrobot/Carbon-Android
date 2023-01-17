@file:OptIn(ExperimentalMaterial3Api::class)

package com.atomicrobot.carbon.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.atomicrobot.carbon.R

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun TransparentTextField(
    modifier: Modifier = Modifier,
    value: String = stringResource(id = R.string.txtField_placeholder),
    labelResId: Int = R.string.label_placeholder,
    onValueChanged: (String) -> Unit = { _ -> }
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
        colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent)
    )
}
