package com.atomicrobot.carbon.ui.designsystem

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

//region Composables
@Composable
fun DesignCheckboxesScreen(
    modifier: Modifier = Modifier,
) {
    var isChecked by remember { mutableStateOf(true) }
    var toggleState by remember { mutableStateOf(ToggleableState.On) }
    LazyColumn(
        modifier = modifier
    ) {
        item {
            CheckboxColumnItem(isChecked) {
                isChecked = it
            }
        }
        item { CheckboxColumnItem(checked = true, enabled = false) }
        item { CheckboxColumnItem(checked = false, enabled = false) }
        item {
            TriStateCheckboxColumnItem(toggleableState = toggleState, enabled = true) {
                toggleState = if (toggleState == ToggleableState.On) ToggleableState.Indeterminate
                else if (toggleState == ToggleableState.Indeterminate) ToggleableState.Off
                else ToggleableState.On
            }
        }
        item {
            TriStateCheckboxColumnItem(toggleableState = ToggleableState.Indeterminate, enabled = false)
        }
    }
}

@Composable
fun CheckboxColumnItem(
    checked: Boolean,
    enabled: Boolean = true,
    onCheckedChange: ((Boolean) -> Unit)? = {}
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = checked, onCheckedChange = onCheckedChange, enabled = enabled)
        Spacer(modifier = Modifier.width(16.dp))
        val text = when (enabled) {
            true -> "Checkbox (Enabled)"
            else -> {
                if (checked) "Checkbox (Disabled/Active)" else "Checkbox (Disabled/Inactive)"
            }
        }
        Text(text = text)
    }
}

@Composable
fun TriStateCheckboxColumnItem(
    toggleableState: ToggleableState,
    enabled: Boolean = true,
    onToggleClicked: (() -> Unit)? = {}
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        TriStateCheckbox(
            state = toggleableState,
            onClick = onToggleClicked,
            enabled = enabled,
        )
        Spacer(modifier = Modifier.width(16.dp))
        val text = when (enabled) {
            true ->
                when (toggleableState) {
                    ToggleableState.On -> "TriState Checkbox (Enabled/Active)"
                    ToggleableState.Indeterminate -> "TriState Checkbox (Enabled/Indeterminate)"
                    else -> "TriState Checkbox (Enabled/Inactive)"
                }
            else -> {
                when (toggleableState) {
                    ToggleableState.On -> "TriState Checkbox (Disabled/Active)"
                    ToggleableState.Indeterminate -> "TriState Checkbox (Disabled/Indeterminate)"
                    else -> "TriState Checkbox (Disabled/Active)"
                }
            }
        }
        Text(text = text)
    }
}
//endregion

//region Composable Previews
@Preview("Checked Checkbox")
@Composable
fun CheckedCheckboxColumnItemPreview() {
    CheckboxColumnItem(checked = true, enabled = true)
}

@Preview("Checked Disabled Checkbox")
@Composable
fun CheckedDisabledCheckboxColumnItemPreview() {
    CheckboxColumnItem(checked = true, enabled = false)
}
//endregion
