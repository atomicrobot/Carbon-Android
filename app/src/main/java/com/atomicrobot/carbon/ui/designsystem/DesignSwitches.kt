package com.atomicrobot.carbon.ui.designsystem

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.atomicrobot.carbon.ui.theme.CarbonAndroidTheme

//region Composables
@Composable
fun DesignSwitchesScreen(
    modifier: Modifier = Modifier,
) {
    var isChecked by remember { mutableStateOf(true) }
    LazyColumn(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 2.dp)
    ) {
        item(key = "Checked/Enabled") {
            SwitchRow(isChecked) {
                isChecked = it
            }
        }
        item(key = "Checked/Disabled") { SwitchRow(checked = true, enabled = false) }
        item(key = "Unchecked/Disabled") { SwitchRow(checked = false, enabled = false) }
    }
}

@Composable
fun SwitchRow(checked: Boolean, enabled: Boolean = true, onCheckedChange: ((Boolean) -> Unit)? = {}) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Switch(checked = checked, onCheckedChange = onCheckedChange, enabled = enabled)
        Spacer(modifier = Modifier.width(16.dp))
        val text = when (enabled) {
            true -> "Switch (Enabled)"
            else -> {
                if (checked) "Switch (Disabled/Active)" else "Switch (Disabled/Inactive)"
            }
        }
        Text(text = text)
    }
}

//endregion

//region Composable Previews
@Preview("Checked Switch")
@Composable
fun CheckedSwitchRowPreview() = CarbonAndroidTheme {
    SwitchRow(checked = true, enabled = true)
}

@Preview("Checked Disabled Switch")
@Composable
fun CheckedDisabledSwitchRowPreview() = CarbonAndroidTheme {
    SwitchRow(checked = true, enabled = false)
}
//endregion
