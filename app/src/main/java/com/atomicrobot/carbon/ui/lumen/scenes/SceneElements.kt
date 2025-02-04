package com.atomicrobot.carbon.ui.lumen.scenes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.data.lumen.SceneModel
import com.atomicrobot.carbon.data.lumen.dto.LumenLight
import com.atomicrobot.carbon.data.lumen.dto.LumenScene
import com.atomicrobot.carbon.data.lumen.dto.RoomNameAndId
import com.atomicrobot.carbon.ui.lumen.LumenSwitch
import com.atomicrobot.carbon.ui.shader.AngledLinearGradient
import com.atomicrobot.carbon.ui.theme.BrightBlurple
import com.atomicrobot.carbon.ui.theme.CardBackgroundOff
import com.atomicrobot.carbon.ui.theme.CardBackgroundOn
import com.atomicrobot.carbon.ui.theme.LightBlurple
import com.atomicrobot.carbon.ui.theme.MediumBlurple
import com.atomicrobot.carbon.ui.theme.White100
import com.atomicrobot.carbon.ui.theme.White3
import com.atomicrobot.carbon.ui.theme.White50

@Composable
fun SceneSectionHeader(
    headerTitle: String = "Favorites",
    modifier: Modifier =
        Modifier
            .fillMaxWidth(),
) {
    Text(
        text = headerTitle,
        modifier =
            modifier
                .fillMaxWidth()
                .padding(top = 14.dp),
        style = MaterialTheme.typography.displayLarge,
    )
}

@Composable
fun SceneItem(
    scene: LumenScene,
    roomName: String = "",
    modifier: Modifier = Modifier.fillMaxWidth(),
    onPlayClicked: () -> Unit,
) {
    Row(
        modifier =
            modifier
                .border(
                    width = 1.dp,
                    brush =
                        AngledLinearGradient(
                            colors = listOf(White50, White3),
                            angleInDegrees = -135F,
                            useAsCssAngle = true,
                        ),
                    shape = MaterialTheme.shapes.medium,
                )
                .background(
                    color = if (scene.active) CardBackgroundOn else CardBackgroundOff,
                    shape = MaterialTheme.shapes.medium,
                )
                .padding(16.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = scene.sceneName,
                    modifier =
                        Modifier
                            .padding(bottom = 8.dp),
                    style = MaterialTheme.typography.displayMedium,
                )
                // Since the non-favorite Scenes are enumerated by the containing 'room',
                // we only need to show the 'room' label for the favorite Scenes
                if (scene.favorite) {
                    Text(
                        text = roomName,
                        modifier =
                            Modifier
                                .padding(bottom = 4.dp),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }

                LeftAlignedIconText(
                    scene.duration,
                    painterResource(
                        id =
                            if (scene.active) {
                                R.drawable.ic_lumen_timer
                            } else {
                                R.drawable.ic_lumen_clock
                            },
                    ),
                    stringResource(id = R.string.duration),
                )
            }
            val imgData =
                if (scene.active) {
                    Pair(R.drawable.ic_lumen_stop_filled, R.string.cont_desc_stop_scene)
                } else {
                    Pair(R.drawable.ic_lumen_play, R.string.cont_desc_start_scene)
                }

            IconButton(onClick = onPlayClicked) {
                Icon(
                    painter = painterResource(imgData.first),
                    contentDescription = stringResource(imgData.second),
                    modifier = Modifier.size(48.dp),
                )
            }
        }
    }
}

@Composable
fun DualActionRow(
    title: String,
    painter: Painter? = null,
    actionContextDescription: String? = null,
    modifier: Modifier = Modifier,
    onClose: () -> Unit = {},
    onAction: () -> Unit = {},
) {
    Row(
        modifier =
            modifier
                .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = { onAction() },
            modifier =
                Modifier
                    .clip(CircleShape),
            enabled = painter != null,
        ) {
            painter?.let {
                Icon(
                    painter = painter,
                    contentDescription = actionContextDescription,
                    modifier =
                        Modifier
                            .size(48.dp)
                            .padding(8.dp),
                )
            }
        }

        Text(
            text = title,
            modifier = Modifier.weight(1F),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.displayLarge,
        )

        IconButton(
            onClick = { onClose() },
            modifier =
                Modifier
                    .clip(CircleShape),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_lumen_close),
                contentDescription = stringResource(id = R.string.cont_desc_menu_close),
                modifier = Modifier.size(48.dp),
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
    onTextChanged: (String) -> Unit = {},
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            modifier =
                Modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
            style = MaterialTheme.typography.bodyMedium,
        )

        TextField(
            value = text,
            onValueChange = onTextChanged,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        brush =
                            AngledLinearGradient(
                                colors = listOf(White50, White3),
                                angleInDegrees = -135F,
                                useAsCssAngle = true,
                            ),
                        shape = MaterialTheme.shapes.medium,
                    )
                    .background(
                        color = CardBackgroundOn,
                        shape = MaterialTheme.shapes.medium,
                    ),
            textStyle = MaterialTheme.typography.bodyLarge,
            placeholder = {
                placeholder?.let {
                    TaskPlaceHolderText(placeholder, Modifier.fillMaxWidth())
                }
            },
            singleLine = true,
            colors =
                TextFieldDefaults.colors(
                    cursorColor = MaterialTheme.colorScheme.onPrimary,
                ),
            shape = RoundedCornerShape(8.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskLabeledDropDownMenu(
    label: String = "Label",
    options: List<Any> = emptyList(),
    selectedOption: Any = if (options.isNotEmpty()) options[0] else "Example Selected Option",
    placeholder: String? = null,
    initiallyExpanded: Boolean = false,
    modifier: Modifier = Modifier,
    onOptionSelected: (Any) -> Unit = {},
) {
    var expanded by remember { mutableStateOf(initiallyExpanded) }

    Column(modifier = modifier) {
        Text(
            text = label,
            modifier =
                Modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
            style = MaterialTheme.typography.bodyMedium,
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
        ) {
            TextField(
                value = selectedOption.toString(),
                onValueChange = { /* Intentionally left blank */ },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            brush =
                                AngledLinearGradient(
                                    colors = listOf(White50, White3),
                                    angleInDegrees = -135F,
                                    useAsCssAngle = true,
                                ),
                            shape = MaterialTheme.shapes.medium,
                        )
                        .background(
                            color = CardBackgroundOn,
                            shape = MaterialTheme.shapes.medium,
                        ),
                readOnly = true,
                textStyle = MaterialTheme.typography.bodyLarge,
                placeholder = {
                    placeholder?.let {
                        TaskPlaceHolderText(placeholder, Modifier.fillMaxWidth())
                    }
                },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                singleLine = true,
                colors =
                    ExposedDropdownMenuDefaults.textFieldColors(
                        cursorColor = White100,
                        disabledTextColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        errorContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                    ),
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.exposedDropdownSize(),
            ) {
                options.forEach { selectionOption ->
                    DropdownMenuItem(
                        onClick = {
                            onOptionSelected(selectionOption)
                            expanded = false
                        },
                        text = {
                            Text(
                                text = selectionOption.toString(),
                                style = MaterialTheme.typography.bodyLarge,
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TaskPlaceHolderText(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier = modifier,
        color = White50,
        style = MaterialTheme.typography.bodyLarge,
    )
}

@Composable
fun LeftAlignedIconText(
    text: String,
    iconPainter: Painter,
    iconContentDescription: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = iconPainter,
            contentDescription = iconContentDescription,
            modifier =
                Modifier
                    .size(24.dp)
                    .padding(end = 8.dp),
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
fun SceneLightItem(
    device: LumenLight,
    checked: Boolean = false,
    modifier: Modifier = Modifier.fillMaxWidth(),
    onLightChecked: (Long, Boolean) -> Unit,
) {
    ConstraintLayout(
        modifier =
            Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    brush =
                        AngledLinearGradient(
                            colors = listOf(White50, White3),
                            angleInDegrees = -135F,
                            useAsCssAngle = true,
                        ),
                    shape = MaterialTheme.shapes.medium,
                )
                .background(
                    color = if (device.active) CardBackgroundOn else CardBackgroundOff,
                    shape = MaterialTheme.shapes.medium,
                )
                .padding(16.dp),
    ) {
        val (lightImage, lightLabel, brightnessLabel, tempLabel, timeLabel, switch) = createRefs()

        Image(
            painter = painterResource(id = R.drawable.ic_lumen_color_bulb),
            contentDescription = stringResource(id = R.string.cont_desc_scene_light),
            modifier =
                Modifier
                    .size(35.dp)
                    .constrainAs(lightImage) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                    },
        )
        LumenSwitch(
            checked = checked,
            onCheckedChange = { onLightChecked(device.lightId, it) },
            modifier =
                Modifier
                    .size(32.dp, 56.dp)
                    .constrainAs(switch) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                    },
            colors =
                SwitchDefaults.colors(
                    checkedThumbColor = White100,
                    checkedTrackColor = BrightBlurple,
                    uncheckedThumbColor = White100,
                    uncheckedTrackColor = MediumBlurple,
                ),
        )

        Text(
            text = device.lightName,
            modifier.constrainAs(lightLabel) {
                start.linkTo(lightImage.end, 8.dp)
                end.linkTo(switch.start, 8.dp)
                width = Dimension.fillToConstraints
            },
            style = MaterialTheme.typography.headlineLarge,
        )

        LeftAlignedIconText(
            text = "${ (100 * device.brightness).toInt() }%",
            iconPainter = painterResource(id = R.drawable.ic_lumen_bright_sun),
            iconContentDescription = stringResource(id = R.string.cont_desc_light_bright),
            modifier =
                Modifier.constrainAs(tempLabel) {
                    start.linkTo(lightLabel.start)
                    top.linkTo(lightLabel.bottom)
                    end.linkTo(lightLabel.end)
                    width = Dimension.fillToConstraints
                },
        )

        LeftAlignedIconText(
            text = device.colorTemperature,
            iconPainter = painterResource(id = R.drawable.ic_lumen_color),
            iconContentDescription = stringResource(id = R.string.cont_desc_light_temp),
            modifier =
                Modifier.constrainAs(brightnessLabel) {
                    start.linkTo(lightLabel.start)
                    top.linkTo(tempLabel.bottom)
                    end.linkTo(lightLabel.end)
                    width = Dimension.fillToConstraints
                },
        )

        LeftAlignedIconText(
            text = "Time",
            iconPainter = painterResource(id = R.drawable.ic_lumen_clock),
            iconContentDescription = stringResource(id = R.string.cont_desc_light_duration),
            modifier =
                Modifier.constrainAs(timeLabel) {
                    start.linkTo(lightLabel.start)
                    top.linkTo(brightnessLabel.bottom)
                    end.linkTo(lightLabel.end)
                    width = Dimension.fillToConstraints
                },
        )
    }
}

@Composable
fun SceneTaskDescription() {
    Column(
        modifier =
            Modifier
                .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 40.dp)
                .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(id = R.string.moody_title),
            style = MaterialTheme.typography.displayMedium,
        )
        Text(
            text = stringResource(id = R.string.moody_description),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
fun SceneTaskFavoriteButton(
    favorite: Boolean,
    modifier: Modifier = Modifier,
    onFavorite: (Boolean) -> Unit = {},
) {
    Button(
        onClick = { onFavorite(!favorite) },
        modifier =
            modifier
                .clip(shape = MaterialTheme.shapes.medium)
                .border(
                    width = 1.dp,
                    color = LightBlurple,
                    shape = MaterialTheme.shapes.medium,
                ),
        elevation = null,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
    ) {
        Icon(
            painter =
                painterResource(
                    id =
                        if (favorite) {
                            R.drawable.ic_lumen_heart_filled
                        } else {
                            R.drawable.ic_lumen_heart
                        },
                ),
            contentDescription = stringResource(id = R.string.cont_desc_scene_favorite),
        )
        Text(
            text = stringResource(id = R.string.add_favorite),
            modifier = Modifier.padding(horizontal = 6.dp),
            style = MaterialTheme.typography.displayLarge,
        )
    }
}

@Composable
fun SceneDetailsButton(
    newScene: Boolean,
    enabled: Boolean = false,
    onClick: () -> Unit = {},
) {
    Button(
        onClick = { onClick() },
        modifier =
            Modifier
                .border(
                    width = 1.dp,
                    brush =
                        AngledLinearGradient(
                            colors = listOf(White50, White3),
                            angleInDegrees = -135F,
                            useAsCssAngle = true,
                        ),
                    shape = MaterialTheme.shapes.small,
                )
                .fillMaxWidth()
                .height(68.dp),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(containerColor = LightBlurple),
        shape = RectangleShape
    ) {
        val buttonText =
            if (newScene) {
                stringResource(id = R.string.create_scene)
            } else {
                stringResource(id = R.string.save_scene)
            }

        Text(
            text = buttonText,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.displayLarge,
        )
    }
}

fun LazyListScope.sceneDetailsFields(
    scene: SceneModel,
    rooms: List<RoomNameAndId> = emptyList(),
    onSceneUpdated: (SceneModel) -> Unit,
) {
    item {
        TaskLabeledTextField(
            label = stringResource(id = R.string.name),
            text = scene.name,
            placeholder = stringResource(id = R.string.name_room),
        ) {
            onSceneUpdated(scene.copy(name = it))
        }

        // We should load a list of rooms
        TaskLabeledDropDownMenu(
            label = stringResource(id = R.string.room),
            options = rooms,
            selectedOption = scene.roomName,
            placeholder = stringResource(id = R.string.select_room),
            modifier = Modifier.padding(vertical = 16.dp),
        ) {
            val room = (it as RoomNameAndId)
            onSceneUpdated(scene.copy(roomId = room.roomId, roomName = room.roomName))
        }

        TaskLabeledDropDownMenu(
            label = stringResource(id = R.string.duration),
            options =
                stringArrayResource(id = R.array.durations)
                    .toList(),
            selectedOption = scene.duration,
            placeholder = stringResource(id = R.string.select_durations),
        ) {
            onSceneUpdated(scene.copy(duration = (it as String)))
        }
    }
}

fun LazyListScope.sceneDetailsFavoriteButton(
    scene: SceneModel,
    onFavorite: (Boolean) -> Unit,
) {
    item {
        SceneTaskFavoriteButton(
            favorite = scene.favorite,
            modifier =
                Modifier
                    .padding(horizontal = 40.dp, vertical = 16.dp)
                    .fillMaxWidth()
                    .height(56.dp),
            onFavorite = onFavorite,
        )
    }
}

fun LazyListScope.sceneDetailsLights(
    allLights: List<LumenLight>,
    sceneLights: List<Long>,
    onLightChecked: (Long, Boolean) -> Unit,
) {
    // light section header
    item { SceneSectionHeader(stringResource(id = R.string.lights)) }
    // Iterate over available light and check the ones enabled for the scene
    items(allLights, { it.lightId }) {
        SceneLightItem(
            it,
            checked = sceneLights.contains(it.lightId),
            onLightChecked = onLightChecked,
        )
    }
}
