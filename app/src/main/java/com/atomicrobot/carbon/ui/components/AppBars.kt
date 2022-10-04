package com.atomicrobot.carbon.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarData
import androidx.compose.material.SnackbarDefaults
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.atomicrobot.carbon.BuildConfig
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.navigation.AppScreens

@Composable
fun TopBar(title: String = "", buttonIcon: ImageVector, onButtonClicked: () -> Unit) {
    TopAppBar(
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
}

@Composable
fun TopBar(title: String = "") = TopAppBar(title = { Text(text = title) })

@Preview
@Composable
fun TopBarPreview() {
    TopBar(title = "Top Bar Title", buttonIcon = Icons.Filled.Menu, onButtonClicked = {})
}

@Preview
@Composable
fun BottomBar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colors.onSurface
                    .copy(alpha = TextFieldDefaults.BackgroundOpacity)
            )
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(
                id = R.string.version_format, BuildConfig.VERSION_NAME
            ),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = stringResource(
                id = R.string.fingerprint_format, BuildConfig.VERSION_FINGERPRINT
            )
        )
    }
}

@Composable
fun BottomNavigationBar(
    navController: NavController,
    destinations: List<AppScreens>,
    onDestinationClicked: (AppScreens) -> Unit
) {
    BottomNavigation {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        destinations.forEach { destination ->
            BottomNavigationItem(
                selected = currentDestination
                    ?.hierarchy
                    ?.any { it.route == destination.route } == true,
                icon = {
                    Icon(
                        destination.iconData.icon,
                        stringResource(id = destination.iconData.iconContentDescription)
                    )
                },
                onClick = {
                    onDestinationClicked(destination)
                }
            )
        }
    }
}

@Preview
@Composable
fun BottomNavigationBarPreview() {
    BottomNavigationBar(
        navController = rememberNavController(),
        destinations = listOf(
            AppScreens.Home,
            AppScreens.Settings,
            AppScreens.Scanner
        ),
        onDestinationClicked = {}
    )
}

@Composable
fun CustomSnackbar(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    SnackbarHost(
        modifier = modifier.fillMaxWidth(),
        hostState = hostState,
        snackbar = { snackbarData: SnackbarData ->
            CustomSnackBarContent(snackbarData.message)
        }
    )
}

@Composable
private fun CustomSnackBarContent(message: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = SnackbarDefaults.backgroundColor
            )
            .padding(16.dp)
    ) {
        Text(
            text = message,
            color = SnackbarDefaults.primaryActionColor
        )
    }
}

@Preview
@Composable
fun CustomSnackBarContentPreview() {
    CustomSnackBarContent(message = "Snackbar sample content")
}
