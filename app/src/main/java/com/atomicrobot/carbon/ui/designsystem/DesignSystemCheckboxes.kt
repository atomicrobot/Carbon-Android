package com.atomicrobot.carbon.ui.designsystem

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.atomicrobot.carbon.ui.theme.CarbonAndroidTheme
import org.koin.androidx.compose.getViewModel

//region Composables
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DesignSystemCheckboxesScreen(
    modifier: Modifier = Modifier,
    designSystemVM: DesignSystemViewModel = getViewModel(),
    onNavIconClicked: () -> Unit,
) {
    val screenState: DesignSystemViewModel.ScreenState by designSystemVM.uiState.collectAsState()
    CarbonAndroidTheme(
        darkTheme = screenState.darkMode,
        fontScale = screenState.fontScale.scale,
    ) {
        Scaffold(
            topBar = {
                DesignScreenAppBar(
                    title = stringResource(id = DesignSystemScreens.Checkboxes.title),
                    screenState.darkMode,
                    selectedFontScale = screenState.fontScale,
                    onBackPressed = onNavIconClicked,
                    onFontScaleChanged = {
                        designSystemVM.updateFontScale(it)
                    },
                    onDarkModeChanged = {
                        designSystemVM.enabledDarkMode(it)
                    }
                )
            },
            modifier = modifier,
        ) {
            var isChecked by remember { mutableStateOf(true) }
            var toggleState by remember { mutableStateOf(ToggleableState.On) }
            LazyColumn(
                modifier = Modifier.padding(it)
            ) {
                item {
                    CheckboxRow(isChecked) {
                        isChecked = it
                    }
                }
                item { CheckboxRow(checked = true, enabled = false) }
                item { CheckboxRow(checked = false, enabled = false) }
                item {
                    TriStateCheckboxRow(toggleableState = toggleState, enabled = true) {
                        toggleState = if (toggleState == ToggleableState.On) ToggleableState.Indeterminate
                        else if (toggleState == ToggleableState.Indeterminate) ToggleableState.Off
                        else ToggleableState.On
                    }
                }
                item {
                    TriStateCheckboxRow(toggleableState = ToggleableState.Indeterminate, enabled = false)
                }
            }
        }
    }
}

@Composable
fun CheckboxRow(
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
fun TriStateCheckboxRow(
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
                    ToggleableState.Off -> "TriState Checkbox (Enabled/Inactive)"
                }
            else -> {
                when (toggleableState) {
                    ToggleableState.On -> "TriState Checkbox (Disabled/Active)"
                    ToggleableState.Indeterminate -> "TriState Checkbox (Disabled/Indeterminate)"
                    ToggleableState.Off -> "TriState Checkbox (Disabled/Active)"
                }
            }
        }
        Text(text = text)
    }
}
//endregion

//region Composable Previews
@Preview("Checked/Enabled")
@Composable
fun CheckedCheckboxRowPreview() = CarbonAndroidTheme {
    CheckboxRow(checked = true, enabled = true)
}

@Preview("Checked/Disabled")
@Composable
fun CheckedDisabledCheckboxRowPreview() = CarbonAndroidTheme {
    CheckboxRow(checked = true, enabled = false)
}
//endregion
