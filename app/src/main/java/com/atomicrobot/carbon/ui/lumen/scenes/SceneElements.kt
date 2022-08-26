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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.data.lumen.Device
import com.atomicrobot.carbon.data.lumen.Scene
import com.atomicrobot.carbon.ui.compose.ScenePreviewProvider
import com.atomicrobot.carbon.ui.shader.AngledLinearGradient
import com.atomicrobot.carbon.ui.theme.CardBackgroundOff
import com.atomicrobot.carbon.ui.theme.CardBackgroundOn
import com.atomicrobot.carbon.ui.theme.White100
import com.atomicrobot.carbon.ui.theme.White3
import com.atomicrobot.carbon.ui.theme.White50

@Preview
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
            style = MaterialTheme.typography.h2)
}

@Preview
@Composable
fun SceneItem(
        @PreviewParameter(ScenePreviewProvider::class, limit = 2) scene: Scene,
        modifier: Modifier = Modifier.fillMaxWidth(),
) {
    Row(
            modifier = modifier
                    .border(
                            width = 1.dp,
                            brush = AngledLinearGradient(
                                    colors = listOf(White50, White3),
                                    angleInDegrees = -135F,
                                    useAsCssAngle = true),
                            shape = RoundedCornerShape(8.dp))
                    .background(
                            color = if (scene.active) CardBackgroundOn else CardBackgroundOff,
                            shape = RoundedCornerShape(8.dp)
                    )
                    .padding(16.dp)
    ) {
        Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                    modifier = Modifier.weight(1f)) {
                Text(
                        text = scene.name,
                        modifier = Modifier
                                .padding(bottom = 8.dp),
                        style = MaterialTheme.typography.h3
                )
                // Since the non-favorite Scenes are enumerated by the containing 'room',
                // we only need to show the 'room' label for the favorite Scenes
                if(scene.favorite)
                    Text(
                            text = scene.room.name,
                            modifier = Modifier
                                    .padding(bottom = 4.dp),
                            style = MaterialTheme.typography.body1
                    )

                DurationLabel(scene.duration, scene.active)
            }
            val imgData = if(scene.active)
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

@Preview
@Composable
fun DurationLabel(
        duration: String = "6 Hoursr",
        active: Boolean = false,
        modifier: Modifier = Modifier
                .fillMaxWidth()) {
    Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically) {
        Icon(
                painter = painterResource(id = if(active)
                    R.drawable.ic_lumen_timer
                else
                    R.drawable.ic_lumen_clock),
                contentDescription = "Duration Icon",
                modifier = Modifier
                        .size(24.dp)
                        .padding(end = 8.dp))
        Text(
                text = duration,
                style = MaterialTheme.typography.body1)
    }
}

@Preview
@Composable
fun DualActionRow(
        title: String = "Title",
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

@Preview
@Composable
fun TaskLabeledTextField(
        label: String = "Label",
        text: String = "Initial Text",
        placeholder: String? = null,
        modifier: Modifier = Modifier,
        onTextChanged:(String) -> Unit = {}
) {
    Column(modifier = modifier) {
        Text(
                text = label,
                modifier = Modifier
                        .padding(start = 32.dp, end = 32.dp, bottom = 8.dp),
                style = MaterialTheme.typography.body2
        )

        TextField(
                value = text,
                onValueChange = {},
                modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .border(
                                width = 1.dp,
                                brush = AngledLinearGradient(
                                        colors = listOf(White50, White3),
                                        angleInDegrees = -135F,
                                        useAsCssAngle = true
                                ),
                                shape = RoundedCornerShape(8.dp)
                        )
                        .background(
                                color = CardBackgroundOn,
                                shape = RoundedCornerShape(8.dp)
                        ),
                textStyle = MaterialTheme.typography.body1,
                placeholder = { TaskPlaceHolderText(placeholder) },
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
@Preview
@Composable
fun TaskLabeledDropDownMenu(
        label: String = "Label",
        options: List<Any> = emptyList(),
        selectedOption: Any = if(options.isNotEmpty()) options[0] else "Example Selected Option",
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
                        .padding(start = 32.dp, end = 32.dp, bottom = 8.dp),
                style = MaterialTheme.typography.body2
        )

        ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }) {
            TextField(
                    value = selectedOption.toString(),
                    onValueChange = { /* Intentionally left blank */ },
                    modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .border(
                                    width = 1.dp,
                                    brush = AngledLinearGradient(
                                            colors = listOf(White50, White3),
                                            angleInDegrees = -135F,
                                            useAsCssAngle = true
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                            )
                            .background(
                                    color = CardBackgroundOn,
                                    shape = RoundedCornerShape(8.dp)
                            ),
                    readOnly = true,
                    textStyle = MaterialTheme.typography.body1,
                    placeholder = { TaskPlaceHolderText(placeholder) },
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

@Preview
@Composable
fun TaskPlaceHolderText(text: String? = "Placeholder Text") {
    text?.let {
        Text(
                text = text,
                color = White50,
                style = MaterialTheme.typography.body1
        )
    }
}

@Composable
fun DeviceItem(device: Device) {

}