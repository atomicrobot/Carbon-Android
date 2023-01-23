package com.atomicrobot.carbon.ui.designsystem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

//region Composables
@Composable
fun ButtonTemplateContent() {

}

enum class ButtonType {
    Filled,
    Outlined,
    Elevated
}

enum class IconButtonType {
    NoFill,
    Filled,
    FilledTonal,
    Outlined,
}

fun LazyListScope.buttonTemplate(
    modifier: Modifier = Modifier,
    button: ButtonType = ButtonType.Filled) {
    items(2) {
        val enabled = it == 0
        val text = if(enabled) "${button.name} Button (Enabled)" else "${button.name} Button (Disabled)"
        when (button) {
            ButtonType.Filled -> Button(onClick = {}, modifier = modifier, enabled = enabled) { Text(text = text) }
            ButtonType.Outlined -> OutlinedButton(onClick = {}, modifier = modifier, enabled = enabled) { Text(text = text) }
            ButtonType.Elevated -> ElevatedButton(onClick = {}, modifier = modifier, enabled = enabled) { Text(text = text) }
        }
    }
}

fun LazyListScope.iconButtonTemplate(
    icon: ImageVector,
    buttonIcon: IconButtonType = IconButtonType.NoFill,
) {
    items(2) {
        val enabled = it == 0
        when (buttonIcon) {
            IconButtonType.NoFill -> {
                IconButton(onClick = { }, enabled = enabled) {
                    Icon(icon, "ImageVector asset name ${icon.name}")
                }
            }
            IconButtonType.Filled -> {
                FilledIconButton(onClick = { }, enabled = enabled) {
                    Icon(icon, "ImageVector asset name ${icon.name}")
                }
            }
            IconButtonType.FilledTonal -> {
                FilledTonalIconButton(onClick = { }, enabled = enabled) {
                    Icon(icon, "ImageVector asset name ${icon.name}")
                }
            }
            IconButtonType.Outlined -> {
                OutlinedIconButton(onClick = { }, enabled = enabled) {
                    Icon(icon, "ImageVector asset name ${icon.name}")
                }
            }
        }
    }
}

fun LazyListScope.fabButtonTemplate(
    icon: ImageVector,
) {
    // Regular, smegular FAB
    item {
        FloatingActionButton(onClick = { }) {
            Icon(icon, "Add")
        }
    }
    // Extended FAB
    item {
        ExtendedFloatingActionButton(onClick = { }) {
            Icon(icon, "ImageVector asset name ${icon.name}")
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Extended FAB")
        }
    }
}

@Composable
fun DesignButtonsScreen(
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 2.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        buttonTemplate(modifier = Modifier.fillMaxWidth())
        buttonTemplate(modifier = Modifier.fillMaxWidth(), button = ButtonType.Outlined)
        buttonTemplate(modifier = Modifier.fillMaxWidth(), button = ButtonType.Elevated)

        iconButtonTemplate(Icons.Filled.ThumbUp)
        iconButtonTemplate(Icons.Filled.ThumbDown, IconButtonType.Filled)
        iconButtonTemplate(Icons.Filled.Settings, IconButtonType.FilledTonal)
        iconButtonTemplate(Icons.Filled.CameraAlt, IconButtonType.Outlined)
        fabButtonTemplate(Icons.Filled.Palette)
    }
}
//endregion
//region Composable  Previews
@Preview("Filled Buttons")
@Composable
fun FilledButtonsPreview() {
    LazyColumn {
        buttonTemplate(modifier = Modifier.fillMaxWidth())
    }
}

@Preview("Outlined Buttons")
@Composable
fun OutlinedButtonsPreview() {
    LazyColumn {
        buttonTemplate(modifier = Modifier.fillMaxWidth(), button = ButtonType.Outlined)
    }
}

@Preview("Elevated Buttons")
@Composable
fun ElevatedButtonsPreview() {
    LazyColumn {
        buttonTemplate(modifier = Modifier.fillMaxWidth(), button = ButtonType.Elevated)
    }
}

@Preview("No Fill Icon Buttons")
@Composable
fun IconButtonsPreview() {
    LazyColumn {
        iconButtonTemplate(Icons.Filled.ThumbUp)
    }
}

@Preview("Filled Icon Buttons")
@Composable
fun FilledIconButtonsPreview() {
    LazyColumn {
        iconButtonTemplate(Icons.Filled.ThumbDown, IconButtonType.Filled)
    }
}

@Preview("Elevated Buttons")
@Composable
fun FilledTonalIconButtonsPreview() {
    LazyColumn {
        iconButtonTemplate(Icons.Filled.Settings, IconButtonType.FilledTonal)
    }
}

@Preview("Outlined Icon Buttons")
@Composable
fun OutlinedIconButtonPreview() {
    LazyColumn {
        iconButtonTemplate(Icons.Filled.CameraAlt, IconButtonType.Outlined)
    }
}
//endregion




