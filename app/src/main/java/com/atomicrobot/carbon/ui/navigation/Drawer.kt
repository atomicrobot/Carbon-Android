package com.atomicrobot.carbon.ui.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.navigation.CarbonScreens

//region Drawer Composables
@Composable
fun Drawer(
    screens: List<CarbonScreens>,
    modifier: Modifier = Modifier,
    onDestinationClicked: (route: CarbonScreens) -> Unit = { _ -> }
) {
    Column(
        modifier
            .fillMaxSize()
            .padding(start = 24.dp, top = 48.dp)
    ) {
        Box(
            Modifier
                .clip(CircleShape)
                .background(
                    Color.Gray
                )
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "App Icon foreground"
            )
        }
        screens.forEach { screen ->
            Spacer(Modifier.height(24.dp))
            DrawerAppScreenItem(screen) {
                onDestinationClicked(screen)
            }
        }
    }
}

@Composable
fun DrawerAppScreenItem(
    screen: CarbonScreens,
    onClicked: () -> Unit = { }
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable { onClicked() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        val contentDesc = stringResource(id = screen.iconData.iconContentDescription)
        val modifier = Modifier
            .size(45.dp)
            .padding(8.dp)
            .align(Alignment.CenterVertically)
        if (screen.route == CarbonScreens.About.route ||
            screen.route == CarbonScreens.AboutHtml.route
        ) {
            Icon(
                painter = painterResource(id = R.drawable.carbon_android_logo), // Use custom icon
                contentDescription = contentDesc,
                modifier = modifier
            )
        } else {
            Icon(
                imageVector = screen.iconData.unselectedIcon,
                contentDescription = contentDesc,
                modifier = modifier
            )
        }
        Text(
            text = screen.title,
            style = MaterialTheme.typography.headlineMedium,
        )
    }
}
//endregion

//region Drawer Composable Previews
@Preview
@Composable
fun DrawerPreview() {
    Drawer(screens = CarbonScreens.values(), onDestinationClicked = { })
}

@Preview
@Composable
fun DrawerAppScreenItemPreview() {
    DrawerAppScreenItem(CarbonScreens.Home)
}
//endregion
