package com.atomicrobot.carbon.ui.designsystem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

//region Composables
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
        buttonItemsTemplate(modifier = Modifier.fillMaxWidth())
        buttonItemsTemplate(modifier = Modifier.fillMaxWidth(), button = ButtonType.Outlined)
        buttonItemsTemplate(modifier = Modifier.fillMaxWidth(), button = ButtonType.Elevated)

        iconButtonItemsTemplate(Icons.Filled.ThumbUp, IconButtonType.NoFill)
        iconButtonItemsTemplate(Icons.Filled.ThumbDown, IconButtonType.Filled)
        iconButtonItemsTemplate(Icons.Filled.Settings, IconButtonType.FilledTonal)
        iconButtonItemsTemplate(Icons.Filled.CameraAlt, IconButtonType.Outlined)
        fabButtonItemsTemplate(Icons.Filled.Palette)

        menuButtonItems()
    }
}
//region LazyColumn Extensions
fun LazyListScope.buttonItemsTemplate(
    modifier: Modifier = Modifier,
    button: ButtonType = ButtonType.Filled
) {
    items(2,
        key = {
            val enabled = it == 0
            val active = if(enabled) "Active" else "Disabled"
            "${button.toString()} Button/$active"
        }
    ) {
        val enabled = it == 0
        val text = if (enabled) "${button.name} Button (Enabled)" else "${button.name} Button (Disabled)"
        when (button) {
            ButtonType.Filled ->
                Button(
                    onClick = {},
                    modifier = modifier,
                    enabled = enabled
                ) { Text(text = text) }
            ButtonType.Outlined ->
                OutlinedButton(
                    onClick = {},
                    modifier = modifier,
                    enabled = enabled
                ) { Text(text = text) }
            ButtonType.Elevated ->
                ElevatedButton(
                    onClick = {},
                    modifier = modifier,
                    enabled = enabled
                ) { Text(text = text) }
        }
    }
}

fun LazyListScope.iconButtonItemsTemplate(
    icon: ImageVector,
    buttonIcon: IconButtonType = IconButtonType.NoFill,
) {
    items(2,
        key = {
            val enabled = it == 0
            val active = if(enabled) "Active" else "Disabled"
            "${buttonIcon.toString()} Icon Button/$active"
        }
    ) {
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

fun LazyListScope.fabButtonItemsTemplate(
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

val menuOptions = listOf("Apple", "Banana", "Chery")

@OptIn(ExperimentalMaterial3Api::class)
fun LazyListScope.menuButtonItems() {
    // Pop-up style menu that gets anchored to sibling composale
    item {
        var expanded by remember { mutableStateOf(false) }
        Box(modifier = Modifier.fillMaxSize()
            .wrapContentSize(Alignment.TopStart)) {
            IconButton(onClick = { expanded = true }) {
                Icon(Icons.Default.MoreVert, contentDescription = "Menu Options")
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                menuOptions.forEach { op ->
                    DropdownMenuItem(
                        text = { Text(op) },
                        onClick = { },
                    )
                }
            }
        }
    }
    // Dropdown menu that displays a menu ontop of a TextField
    item {
        var expanded by remember { mutableStateOf(false) }
        var selectedOptions by rememberSaveable { mutableStateOf("") }
        ExposedDropdownMenuBox(
            expanded = expanded,
            modifier = Modifier.size(195.dp, 56.dp),
            onExpandedChange = { expanded = it }
        ) {
            TextField(
                // The `menuAnchor` modifier must be passed to the text field for correctness.
                modifier = Modifier.menuAnchor(),
                readOnly = true,
                // Force the text style to default so that the text stays the same size
                textStyle = TextStyle.Default,
                value = selectedOptions,
                onValueChange = {},
                trailingIcon = { TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                menuOptions.forEach { op ->
                    DropdownMenuItem(
                        text = {
                            Text(text = op)
                        },
                        onClick = {
                            expanded = false
                            selectedOptions = op
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }
        }
    }
}

//endregion

//endregion

//region Composable  Previews
@Preview("Filled Buttons")
@Composable
fun FilledButtonsPreview() {
    LazyColumn {
        buttonItemsTemplate(modifier = Modifier.fillMaxWidth())
    }
}

@Preview("Outlined Buttons")
@Composable
fun OutlinedButtonsPreview() {
    LazyColumn {
        buttonItemsTemplate(modifier = Modifier.fillMaxWidth(), button = ButtonType.Outlined)
    }
}

@Preview("Elevated Buttons")
@Composable
fun ElevatedButtonsPreview() {
    LazyColumn {
        buttonItemsTemplate(modifier = Modifier.fillMaxWidth(), button = ButtonType.Elevated)
    }
}

@Preview("No Fill Icon Buttons")
@Composable
fun IconButtonsPreview() {
    LazyColumn {
        iconButtonItemsTemplate(Icons.Filled.ThumbUp)
    }
}

@Preview("Filled Icon Buttons")
@Composable
fun FilledIconButtonsPreview() {
    LazyColumn {
        iconButtonItemsTemplate(Icons.Filled.ThumbDown, IconButtonType.Filled)
    }
}

@Preview("Elevated Buttons")
@Composable
fun FilledTonalIconButtonsPreview() {
    LazyColumn {
        iconButtonItemsTemplate(Icons.Filled.Settings, IconButtonType.FilledTonal)
    }
}

@Preview("Outlined Icon Buttons")
@Composable
fun OutlinedIconButtonPreview() {
    LazyColumn {
        iconButtonItemsTemplate(Icons.Filled.CameraAlt, IconButtonType.Outlined)
    }
}

@Preview("Menu Buttons")
@Composable
fun MenuButtonItemsPreview() {
    LazyColumn {
        menuButtonItems()
    }
}
//endregion
