package com.atomicrobot.carbon.ui.designsystem

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.atomicrobot.carbon.ui.theme.CarbonAndroidTheme
import org.koin.androidx.compose.getViewModel

//region Composables
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DesignSystemSlidersScreen(
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
                    title = stringResource(id = DesignSystemScreens.Sliders.title),
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
            LazyColumn(
                Modifier
                    .padding(it)
                    .padding(horizontal = 16.dp, vertical = 2.dp)
            ) {
                item(key = "No-Steps/Enabled") {
                    var sliderValue by remember { mutableStateOf(0.5F) }
                    SliderRow(
                        sliderValue = sliderValue,
                        sliderSteps = 0,
                        sliderEnabled = true,
                    ) {
                        sliderValue = it
                    }
                }

                item(key = "No-Steps/Disabled") {
                    SliderRow(
                        sliderValue = .5f,
                        sliderSteps = 0,
                        sliderEnabled = false,
                    )
                }

                item(key = "Steps/Enabled") {
                    var sliderValue by remember { mutableStateOf(0.333F) }
                    SliderRow(
                        sliderValue = sliderValue,
                        sliderSteps = 5,
                        sliderEnabled = true,
                    ) {
                        sliderValue = it
                    }
                }

                item(key = "Steps/Disabled") {
                    SliderRow(
                        sliderValue = .333f,
                        sliderSteps = 5,
                        sliderEnabled = false,
                    )
                }
            }
        }
    }
}

@Composable
fun SliderRow(
    sliderValue: Float = .0F,
    sliderSteps: Int = 0,
    sliderEnabled: Boolean = true,
    onSliderValueChange: (Float) -> Unit = {},
) {
    Slider(
        value = sliderValue,
        onValueChange = onSliderValueChange,
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth(),
        enabled = sliderEnabled,
        steps = sliderSteps,
    )
}
//endregion
//region Composable  Previews
@Preview("Slider: 50%")
@Composable
fun NoStepSliderSliderRowPreview() = CarbonAndroidTheme {
    SliderRow(
        sliderValue = .5f,
        sliderSteps = 0,
    )
}

@Preview("Stepped Slider: 40%")
@Composable
fun SteppedSliderSliderRowPreview() = CarbonAndroidTheme {
    SliderRow(
        sliderValue = .4f,
        sliderSteps = 4,
    )
}
//endregion
