package com.atomicrobot.carbon.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.ui.lumen.navigation.LumenBottomSheetTask
import com.atomicrobot.carbon.ui.theme.DarkBlurple
import com.atomicrobot.carbon.ui.theme.LightBlurple
import com.atomicrobot.carbon.ui.theme.ScreenHeading

@Preview
@Composable
fun TopBar(
    title: String = CarbonScreens.Home.title,
    buttonIcon: ImageVector = Icons.Filled.Menu,
    onButtonClicked: () -> Unit = {}
) = TopAppBar(
    title = {
        Text(text = title)
    },
    navigationIcon = {
        IconButton(onClick = { onButtonClicked() }) {
            Icon(buttonIcon, contentDescription = "")
        }
    },
    backgroundColor = MaterialTheme.colors.primaryVariant
)

@Preview
@Composable
fun LumenTopAppBar(
    title: String = LumenScreens.Home.title,
    showAction: Boolean = true,
    bottomSheetTasks: List<LumenBottomSheetTask.LumenMenuTask> = listOf(
        LumenBottomSheetTask.AddScene,
    ),
    onTaskSelected: (LumenBottomSheetTask) -> Unit = {}
) = TopAppBar(
    title = {
        Text(
            text = title,
            color = Color.White,
            style = MaterialTheme.typography.ScreenHeading
        )
    },
    actions = {
        if (showAction && bottomSheetTasks.isNotEmpty()) {
            var expanded by remember { mutableStateOf(false) }

            Box(
                modifier = Modifier
                    .wrapContentSize(Alignment.TopEnd)
            ) {
                IconButton(
                    onClick = { expanded = true },
                    modifier = Modifier
                        .size(48.dp)
                        .border(width = 2.dp, color = LightBlurple, shape = CircleShape)
                        .background(color = Color.White, shape = CircleShape)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_lumen_add),
                        contentDescription = stringResource(id = R.string.cont_desc_scene_add),
                        colorFilter = ColorFilter.tint(DarkBlurple)
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .width(450.dp).padding(32.dp)
                ) {
                    bottomSheetTasks.forEach {
                        DropdownMenuItem(
                            onClick = {
                                onTaskSelected(it)
                                expanded = false
                            }
                        ) {
                            Text(
                                text = stringResource(id = it.titleRes),
                                modifier = Modifier.padding(horizontal = 8.dp),
                                style = MaterialTheme.typography.h3
                            )
                        }
                    }
                }
            }
        }
    },
    backgroundColor = Color.Transparent,
    elevation = 0.dp
)
