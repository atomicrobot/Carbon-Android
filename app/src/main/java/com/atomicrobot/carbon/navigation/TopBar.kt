package com.atomicrobot.carbon.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.ui.theme.DarkBlurple
import com.atomicrobot.carbon.ui.theme.ScreenHeading

@Preview
@Composable
fun TopBar(
        title: String = CarbonScreens.Home.title,
        buttonIcon: ImageVector = Icons.Filled.Menu,
        onButtonClicked: () -> Unit = {}
) = TopAppBar(
        title = {
            Text(
                    text = title
            )
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
        showAction: Boolean = false,
        onAction: () -> Unit = {}
) = TopAppBar(
        title = {
            Text(
                text = title,
                color = Color.White,
                style = MaterialTheme.typography.ScreenHeading
            )
        },
        actions = {
            if(showAction) {
                Image(
                    painter = painterResource(id = R.drawable.ic_lumen_add),
                    contentDescription = stringResource(id = R.string.cont_desc_scene_add),
                    modifier = Modifier
                            .clickable { onAction() }
                            .size(48.dp)
                            .background(color = Color.White, shape = CircleShape)
                            .padding(12.dp),
                    colorFilter = ColorFilter.tint(DarkBlurple)
                )
            }
        },
        backgroundColor = Color.Transparent,
        elevation = 0.dp
)