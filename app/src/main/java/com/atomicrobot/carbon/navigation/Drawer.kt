package com.atomicrobot.carbon.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.data.api.github.model.Commit
import com.atomicrobot.carbon.ui.compose.AppScreenPreviewProvider
import com.atomicrobot.carbon.ui.compose.CommitPreviewProvider

@Preview
@Composable
fun Drawer(
    modifier: Modifier = Modifier,
    screens: List<AppScreens> = emptyList(),
    onDestinationClicked: (route: String) -> Unit = { _ -> }) {
    Column(
        modifier
            .fillMaxSize()
            .padding(start = 24.dp, top = 48.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "App Icon"
        )
        screens.forEach { screen ->
            Spacer(Modifier.height(24.dp))
            DrawerAppScreenItem(screen, onDestinationClicked)
        }
    }
}

@Preview
@Composable
fun DrawerAppScreenItem(
    @PreviewParameter(AppScreenPreviewProvider::class, limit = 2) screen: AppScreens,
    onDestinationClicked: (route: String) -> Unit = { _ -> }
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable {
            onDestinationClicked(screen.route)
        },
        verticalAlignment = Alignment.CenterVertically) {
        Icon(
            screen.iconData.icon,
            stringResource(id = screen.iconData.iconContentDescription),
            modifier = Modifier.padding(8.dp)
        )
        Text(
            text = screen.title,
            style = MaterialTheme.typography.h4,
        )
    }
}
