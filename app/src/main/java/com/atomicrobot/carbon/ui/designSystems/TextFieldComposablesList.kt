package com.atomicrobot.carbon.ui.designSystems

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.atomicrobot.carbon.ui.components.AtomicRobotUI


@Composable
fun getTextFieldComposables(): List<@Composable () -> Unit> {

    return listOf { TextFieldComposableContentItem(section = TextFieldType.TRANSPARENT_TEXT_FIELD) }
}

@Composable
fun TextFieldComposableContentItem(
    section: TextFieldType
) {

    Column(
        modifier = Modifier
            .padding(vertical = 16.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = section.sectionTitle,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        AtomicRobotUI.TextField.TransparentTextField(
            label = section.label,
            value = section.placeholder
        )
    }
}

enum class TextFieldType(
    val sectionTitle: String,
    val label: String,
    val placeholder: String
) {
    TRANSPARENT_TEXT_FIELD(
        sectionTitle = "Carbon Transparent TextField",
        label = "Label",
        placeholder = "Placeholder"
    )
}