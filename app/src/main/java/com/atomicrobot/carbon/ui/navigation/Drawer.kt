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
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.navigation.AppScreens
import com.atomicrobot.carbon.util.AppScreenPreviewProvider

@Preview
@Composable
fun Drawer(
    modifier: Modifier = Modifier,
    screens: List<AppScreens> = emptyList(),
    onDestinationClicked: (route: String) -> Unit = { _ -> }
) {
    Column(
        modifier
            .fillMaxSize()
            .padding(start = 24.dp, top = 48.dp)
    ) {
        Box(
            Modifier.clip(CircleShape)
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
            DrawerAppScreenItem(screen, onDestinationClicked)
        }
    }
}

@Preview
@Composable
fun DrawerAppScreenItem(
    @PreviewParameter(AppScreenPreviewProvider::class, limit = 3) screen: AppScreens,
    onDestinationClicked: (route: String) -> Unit = { _ -> }
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable
            {
                onDestinationClicked(screen.route)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            screen.iconData.icon,
            stringResource(id = screen.iconData.iconContentDescription),
            modifier = Modifier.size(45.dp)
                .padding(8.dp)
                .align(Alignment.CenterVertically)
        )
        Text(
            text = screen.title,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h4,
        )
    }
}
