package com.atomicrobot.carbon.ui.designsystem

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.atomicrobot.carbon.ui.theme.CarbonAndroidTheme

//region Composables
@Composable
fun DesignSlidersScreen(modifier: Modifier = Modifier) {

    LazyColumn(modifier = modifier) {
        item {
            var sliderValue by remember { mutableStateOf(0.5F) }
            SliderColumnItem(
                sliderValue = sliderValue,
                sliderSteps = 0,
                sliderEnabled = true,
            ) {
                sliderValue = it
            }
        }

        item {
            SliderColumnItem(
                sliderValue = .5f,
                sliderSteps = 0,
                sliderEnabled = false,
            )
        }

        item {
            var sliderValue by remember { mutableStateOf(0.333F) }
            SliderColumnItem(
                sliderValue = sliderValue,
                sliderSteps = 5,
                sliderEnabled = true,
            ) {
                sliderValue = it
            }
        }

        item {
            SliderColumnItem(
                sliderValue = .333f,
                sliderSteps = 5,
                sliderEnabled = false,
            )
        }
    }
}

@Composable
fun SliderColumnItem(
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
@Preview("Slider progress 50%")
@Composable
fun NoStepSliderColumnItemPreview() {
    CarbonAndroidTheme {
        SliderColumnItem(
            sliderValue = .5f,
            sliderSteps = 0,
        )
    }
}

@Preview("Stepped Slider progress 40%")
@Composable
fun SteppedSliderColumnItemPreview() {
    CarbonAndroidTheme {
        SliderColumnItem(
            sliderValue = .4f,
            sliderSteps = 4,
        )
    }
}
//endregion