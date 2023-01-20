package com.atomicrobot.carbon.ui.designSystems

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun getButtonComposables(): List<@Composable () -> Unit> {

    return listOf(
        { ButtonComposableContentItem(section = ButtonType.BUTTON) },
        { ButtonComposableContentItem(section = ButtonType.ELEVATED_BUTTON) },
        { ButtonComposableContentItem(section = ButtonType.FILLED_TONAL_BUTTON) },
        { ButtonComposableContentItem(section = ButtonType.OUTLINED_BUTTON) },
        { ButtonComposableContentItem(section = ButtonType.TEXT_BUTTON) }
    )
}

@Composable
fun ButtonComposableContentItem(
    section: ButtonType
) {

    val textContent: @Composable () -> Unit = {
        Text(text = "Button")
    }

    val iconContent: @Composable () -> Unit = {
        Icon(
            imageVector = Icons.Filled.Image,
            contentDescription = "Button Icon"
        )
    }

    Column(
        modifier = Modifier.padding(vertical = 16.dp)
    ) {
        Text(
            text = section.sectionTitle,
            style = MaterialTheme.typography.headlineSmall
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SelectedButton(
                modifier = Modifier.weight(1f),
                buttonType = section,
                content = textContent
            )

            Divider(
                modifier = Modifier
                    .height(32.dp)
                    .padding(horizontal = 16.dp)
                    .width(1.dp)
            )

            SelectedButton(
                modifier = Modifier.weight(1f),
                buttonType = section,
                enabled = false,
                content = textContent
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            SelectedButton(
                buttonType = section,
                content = iconContent
            )

            Divider(
                modifier = Modifier
                    .height(32.dp)
                    .padding(horizontal = 16.dp)
                    .width(1.dp)
            )

            SelectedButton(
                buttonType = section,
                enabled = false,
                content = iconContent
            )
        }
    }
}

@Composable
fun SelectedButton(
    modifier: Modifier = Modifier,
    buttonType: ButtonType,
    enabled: Boolean = true,
    content: @Composable () -> Unit
) {
    when (buttonType) {
        ButtonType.BUTTON -> {
            Button(
                modifier = modifier,
                onClick = {},
                enabled = enabled,
                content = { content() }
            )
        }
        ButtonType.ELEVATED_BUTTON -> {
            ElevatedButton(
                modifier = modifier,
                onClick = {},
                enabled = enabled,
                content = { content() }
            )
        }
        ButtonType.FILLED_TONAL_BUTTON -> {
            FilledTonalButton(
                modifier = modifier,
                onClick = {},
                enabled = enabled,
                content = { content() }
            )
        }
        ButtonType.OUTLINED_BUTTON -> {
            OutlinedButton(
                modifier = modifier,
                onClick = {},
                enabled = enabled,
                content = { content() }
            )
        }
        ButtonType.TEXT_BUTTON -> {
            TextButton(
                modifier = modifier,
                onClick = {},
                enabled = enabled,
                content = { content() }
            )
        }
        else -> {}
    }
}

enum class ButtonType(val sectionTitle: String) {
    BUTTON("Basic Button"),
    ELEVATED_BUTTON("Elevated Button"),
    FILLED_TONAL_BUTTON("Filled Tonal Button"),
    OUTLINED_BUTTON("Outlined Button"),
    TEXT_BUTTON("Text Button")
}