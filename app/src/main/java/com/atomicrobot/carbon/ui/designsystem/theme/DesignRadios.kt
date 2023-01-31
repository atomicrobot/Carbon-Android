package com.atomicrobot.carbon.ui.designsystem.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.ui.designsystem.DesignScreenAppBar
import com.atomicrobot.carbon.ui.designsystem.DesignSystemScreens
import com.atomicrobot.carbon.ui.designsystem.DesignSystemViewModel
import com.atomicrobot.carbon.ui.theme.CarbonAndroidTheme
import org.koin.androidx.compose.getViewModel

//region Composables
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DesignSystemRadiosScreen(
    modifier: Modifier = Modifier,
    designSystemVM: DesignSystemViewModel = getViewModel(),
    onNavIconClicked: () -> Unit
) {
    val screenState: DesignSystemViewModel.ScreenState by designSystemVM.uiState.collectAsState()
    CarbonAndroidTheme(
        darkTheme = screenState.darkMode,
        fontScale = screenState.fontScale.scale,
    ) {
        Scaffold(
            topBar = {
                DesignScreenAppBar(
                    title = stringResource(id = DesignSystemScreens.Radios.title),
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
            val radioOptions = stringArrayResource(id = R.array.design_radio_options)
            val (selectedOption, onOptionSelected: (String) -> Unit) = remember { mutableStateOf(radioOptions[0]) }
            Column(
                modifier = Modifier
                    .padding(it)
                    .padding(horizontal = 16.dp, vertical = 2.dp)
            ) {
                Column(Modifier.selectableGroup()) {
                    radioOptions.forEach { text ->
                        RadioRow(
                            radioText = text,
                            selected = (text == selectedOption),
                            onOptionSelected = onOptionSelected,
                            enabled = true
                        )
                    }
                }

                Column(Modifier.selectableGroup()) {
                    RadioRow(
                        radioText = stringResource(id = R.string.design_radio_dis_act),
                        selected = true,
                        onOptionSelected = { },
                        enabled = false,
                    )

                    RadioRow(
                        radioText = stringResource(id = R.string.design_radio_dis_inact),
                        selected = false,
                        onOptionSelected = { },
                        enabled = false,
                    )
                }
            }
        }
    }
}

@Composable
fun RadioRow(
    radioText: String,
    selected: Boolean = false,
    onOptionSelected: (String) -> Unit,
    enabled: Boolean = true,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(56.dp)
            .selectable(
                selected = selected,
                onClick = { onOptionSelected(radioText) },
                role = Role.RadioButton
            )
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = null, // null recommended for accessibility with screenreaders,
            enabled = enabled,
        )
        Text(
            text = radioText,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

//endregion

//region Composable  Previews
@Preview("Checked Radio Button")
@Composable
fun CheckedRadioRowPreview() {
    RadioRow(
        radioText = "Radio Button",
        selected = true,
        onOptionSelected = { },
        enabled = false,
    )
}

@Preview("Unchecked Radio Button")
@Composable
fun UncheckedRadioRowPreview() {
    RadioRow(
        radioText = "Radio Button",
        selected = false,
        onOptionSelected = { },
        enabled = false,
    )
}
//endregion
