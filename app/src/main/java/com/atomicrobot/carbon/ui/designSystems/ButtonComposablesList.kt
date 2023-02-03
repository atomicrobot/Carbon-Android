package com.atomicrobot.carbon.ui.designSystems


import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.atomicrobot.carbon.ui.components.AtomicRobotUI


@Composable
fun getButtonComposables(): List<@Composable () -> Unit> {

    return listOf(
        { ButtonComposableContentItem(section = ButtonType.CARBON_OUTLINED_BUTTON) },
        { ButtonComposableContentItem(section = ButtonType.CARBON_ICON_BUTTON) },
        { ButtonComposableContentItem(section = ButtonType.FILLED_TONAL_BUTTON) }
    )
}

@Composable
fun ButtonComposableContentItem(
    section: ButtonType
) {

    Column(
        modifier = Modifier.padding(vertical = 16.dp).fillMaxWidth()
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
                content = section.content
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
                content = section.content
            )
        }
    }
}

@Composable
fun SelectedButton(
    modifier: Modifier = Modifier,
    buttonType: ButtonType,
    enabled: Boolean = true,
    content: Any
) {
    when (buttonType) {
        ButtonType.CARBON_OUTLINED_BUTTON -> {
            AtomicRobotUI.Button.Outlined(
                onClick = {},
                enabled = enabled,
                text = (content as? String) ?: ""
            )
        }
        ButtonType.CARBON_ICON_BUTTON -> {
            AtomicRobotUI.Button.Icon(
                onClick = {},
                enabled = enabled,
                imageVector = (content as? ImageVector) ?: Icons.Filled.Image
            )
        }
        ButtonType.FILLED_TONAL_BUTTON -> {
            FilledTonalButton(
                modifier = modifier,
                onClick = {},
                enabled = enabled,
                content = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Image,
                            contentDescription = "Button Icon"
                        )
                        Text(text = "Button")
                    }
                }
            )
        }
    }
}

enum class ButtonType(
    val sectionTitle: String,
    val content: Any
    ) {
    CARBON_OUTLINED_BUTTON(
        sectionTitle = "Carbon Outlined Button",
        content = "Button"
    ),
    CARBON_ICON_BUTTON(
        sectionTitle = "Carbon Icon Button",
        content = Icons.Filled.Image
    ),
    FILLED_TONAL_BUTTON(
        sectionTitle = "Filled Tonal Button",
        content = Unit
    )
}