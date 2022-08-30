package com.atomicrobot.carbon.ui.lumen.scenes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.ui.compose.ScenePreviewProvider
import com.atomicrobot.carbon.ui.lumen.LumenSwitch
import com.atomicrobot.carbon.ui.lumen.model.LightModel
import com.atomicrobot.carbon.ui.lumen.model.SceneModel
import com.atomicrobot.carbon.ui.shader.AngledLinearGradient
import com.atomicrobot.carbon.ui.theme.BrightBlurple
import com.atomicrobot.carbon.ui.theme.CardBackgroundOff
import com.atomicrobot.carbon.ui.theme.CardBackgroundOn
import com.atomicrobot.carbon.ui.theme.MediumBlurple
import com.atomicrobot.carbon.ui.theme.White100
import com.atomicrobot.carbon.ui.theme.White3
import com.atomicrobot.carbon.ui.theme.White50

@Composable
fun SceneSectionHeader(
    headerTitle: String = "Favorites",
    modifier: Modifier = Modifier
        .fillMaxWidth()
) {
    Text(
        text = headerTitle,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 14.dp),
        style = MaterialTheme.typography.h2
    )
}

@Composable
fun SceneItem(
    @PreviewParameter(ScenePreviewProvider::class, limit = 2) scene: SceneModel,
    modifier: Modifier = Modifier.fillMaxWidth(),
) {
    Row(
        modifier = modifier
            .border(
                width = 1.dp,
                brush = AngledLinearGradient(
                    colors = listOf(White50, White3),
                    angleInDegrees = -135F,
                    useAsCssAngle = true
                ),
                shape = MaterialTheme.shapes.medium
            )
            .background(
                color = if (scene.active) CardBackgroundOn else CardBackgroundOff,
                shape = MaterialTheme.shapes.medium
            )
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = scene.name,
                    modifier = Modifier
                        .padding(bottom = 8.dp),
                    style = MaterialTheme.typography.h3
                )
                // Since the non-favorite Scenes are enumerated by the containing 'room',
                // we only need to show the 'room' label for the favorite Scenes
                if (scene.favorite)
                    Text(
                        text = scene.owningRoomName,
                        modifier = Modifier
                            .padding(bottom = 4.dp),
                        style = MaterialTheme.typography.body1
                    )

                LeftAlignedIconText(
                    scene.duration,
                    painterResource(
                        id = if (scene.active)
                            R.drawable.ic_lumen_timer
                        else
                            R.drawable.ic_lumen_clock
                    ),
                    stringResource(id = R.string.duration)
                )
            }
            val imgData = if (scene.active)
                Pair(R.drawable.ic_lumen_stop_filled, R.string.cont_desc_stop_scene)
            else
                Pair(R.drawable.ic_lumen_play, R.string.cont_desc_start_scene)

            Image(
                painter = painterResource(imgData.first),
                contentDescription = stringResource(imgData.second),
                modifier = Modifier.size(48.dp)
            )
        }
    }
}

@Composable
fun DualActionRow(
    title: String,
    painter: Painter? = null,
    actionContextDescription: String? = null,
    onClose: () -> Unit = {},
    onAction: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { onAction() },
            modifier = Modifier.clip(CircleShape),
            enabled = painter != null
        ) {
            painter?.let {
                Image(
                    painter = painter,
                    contentDescription = actionContextDescription
                )
            }
        }

        Text(
            text = title,
            modifier = Modifier.weight(1F),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h2
        )

        IconButton(
            onClick = { onClose() },
            modifier = Modifier.clip(CircleShape)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_lumen_close),
                contentDescription = stringResource(id = R.string.cont_desc_menu_close)
            )
        }
    }
}

@Composable
fun TaskLabeledTextField(
    label: String,
    text: String,
    placeholder: String? = null,
    modifier: Modifier = Modifier,
    onTextChanged: (String) -> Unit = {}
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
            style = MaterialTheme.typography.body2
        )

        TextField(
            value = text,
            onValueChange = onTextChanged,
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    brush = AngledLinearGradient(
                        colors = listOf(White50, White3),
                        angleInDegrees = -135F,
                        useAsCssAngle = true
                    ),
                    shape = MaterialTheme.shapes.medium
                )
                .background(
                    color = CardBackgroundOn,
                    shape = MaterialTheme.shapes.medium
                ),
            textStyle = MaterialTheme.typography.body1,
            placeholder = {
                placeholder?.let {
                    TaskPlaceHolderText(placeholder, Modifier.fillMaxWidth())
                }
            },
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = White100,
                disabledTextColor = Color.Transparent,
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TaskLabeledDropDownMenu(
    label: String = "Label",
    options: List<Any> = emptyList(),
    selectedOption: Any = if (options.isNotEmpty()) options[0] else "Example Selected Option",
    placeholder: String? = null,
    initiallyExpanded: Boolean = false,
    modifier: Modifier = Modifier,
    onOptionSelected: (Any) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(initiallyExpanded) }

    Column(modifier = modifier) {
        Text(
            text = label,
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
            style = MaterialTheme.typography.body2
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = selectedOption.toString(),
                onValueChange = { /* Intentionally left blank */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        brush = AngledLinearGradient(
                            colors = listOf(White50, White3),
                            angleInDegrees = -135F,
                            useAsCssAngle = true
                        ),
                        shape = MaterialTheme.shapes.medium
                    )
                    .background(
                        color = CardBackgroundOn,
                        shape = MaterialTheme.shapes.medium
                    ),
                readOnly = true,
                textStyle = MaterialTheme.typography.body1,
                placeholder = {
                    placeholder?.let {
                        TaskPlaceHolderText(placeholder, Modifier.fillMaxWidth())
                    }
                },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                singleLine = true,
                colors = ExposedDropdownMenuDefaults.textFieldColors(
                    cursorColor = White100,
                    disabledTextColor = Color.Transparent,
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.exposedDropdownSize()
            ) {
                options.forEach { selectionOption ->
                    DropdownMenuItem(
                        onClick = {
                            onOptionSelected(selectionOption)
                            expanded = false
                        }
                    ) {
                        Text(
                            text = selectionOption.toString(),
                            style = MaterialTheme.typography.body1
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TaskPlaceHolderText(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier,
        color = White50,
        style = MaterialTheme.typography.body1
    )
}

@Composable
fun LeftAlignedIconText(
    text: String,
    iconPainter: Painter,
    iconContentDescription: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = iconPainter,
            contentDescription = iconContentDescription,
            modifier = Modifier
                .size(24.dp)
                .padding(end = 8.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.body1
        )
    }
}

@Composable
fun SceneLightItem(
    device: LightModel,
    modifier: Modifier = Modifier.fillMaxWidth(),
) {
    ConstraintLayout(
        modifier = Modifier.fillMaxWidth()
            .border(
                width = 1.dp,
                brush = AngledLinearGradient(
                    colors = listOf(White50, White3),
                    angleInDegrees = -135F,
                    useAsCssAngle = true
                ),
                shape = MaterialTheme.shapes.medium
            )
            .background(
                color = if (device.active) CardBackgroundOn else CardBackgroundOff,
                shape = MaterialTheme.shapes.medium
            )
            .padding(16.dp)
    ) {
        val (lightImage, lightLabel, brightnessLabel, tempLabel, timeLabel, switch) = createRefs()

        Image(
            painter = painterResource(id = R.drawable.ic_lumen_color_bulb),
            contentDescription = stringResource(id = R.string.cont_desc_scene_light),
            modifier = Modifier
                .size(35.dp)
                .constrainAs(lightImage) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                }
        )
        LumenSwitch(
            checked = device.active,
            onCheckedChange = {},
            modifier = Modifier
                .size(32.dp, 56.dp)
                .constrainAs(switch) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                },
            colors = SwitchDefaults.colors(
                checkedThumbColor = White100,
                checkedTrackColor = BrightBlurple,
                uncheckedThumbColor = White100,
                uncheckedTrackColor = MediumBlurple,
            )
        )

        Text(
            text = device.name,
            modifier.constrainAs(lightLabel) {
                start.linkTo(lightImage.end, 8.dp)
                end.linkTo(switch.start, 8.dp)
                width = Dimension.fillToConstraints
            },
            style = MaterialTheme.typography.h4
        )

        LeftAlignedIconText(
            text = "${ (100 * device.brightness) }$",
            iconPainter = painterResource(id = R.drawable.ic_lumen_bright_sun),
            iconContentDescription = stringResource(id = R.string.cont_desc_light_bright),
            modifier = Modifier.constrainAs(tempLabel) {
                start.linkTo(lightLabel.start)
                top.linkTo(lightLabel.bottom)
                end.linkTo(lightLabel.end)
                width = Dimension.fillToConstraints
            }
        )

        LeftAlignedIconText(
            text = device.colorTemperature,
            iconPainter = painterResource(id = R.drawable.ic_lumen_color),
            iconContentDescription = stringResource(id = R.string.cont_desc_light_temp),
            modifier = Modifier.constrainAs(brightnessLabel) {
                start.linkTo(lightLabel.start)
                top.linkTo(tempLabel.bottom)
                end.linkTo(lightLabel.end)
                width = Dimension.fillToConstraints
            }
        )

        LeftAlignedIconText(
            text = "Time",
            iconPainter = painterResource(id = R.drawable.ic_lumen_clock),
            iconContentDescription = stringResource(id = R.string.cont_desc_light_duration),
            modifier = Modifier.constrainAs(timeLabel) {
                start.linkTo(lightLabel.start)
                top.linkTo(brightnessLabel.bottom)
                end.linkTo(lightLabel.end)
                width = Dimension.fillToConstraints
            }
        )
    }
}
