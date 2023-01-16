package com.atomicrobot.carbon.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.atomicrobot.carbon.BuildConfig
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.navigation.CarbonScreens
import com.atomicrobot.carbon.util.AppScreensPreviewProvider

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun TopBar(
    title: String = CarbonScreens.Home.title,
    buttonIcon: ImageVector = Icons.Filled.Menu,
    onButtonClicked: () -> Unit = {}
) = TopAppBar(
    title = { Text(text = title) },
    navigationIcon = {
        IconButton(onClick = { onButtonClicked() }) {
            Icon(imageVector = buttonIcon, contentDescription = "")
        }
    },
    actions = {
        Icon(
            painter = painterResource(id = R.drawable.carbon_android_logo),
            contentDescription = stringResource(id = R.string.cont_desc_shell),
            modifier = Modifier.size(24.dp),
            tint = Color.Unspecified
        )
    }
)

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    buildVersion: String,
    fingerprint: String
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.onSurface)
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.version_format, buildVersion),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = stringResource(id = R.string.fingerprint_format, fingerprint)
        )
    }
}

@Preview
@Composable
fun BottomBarPreview() {
    BottomBar(
        buildVersion = BuildConfig.VERSION_NAME,
        fingerprint = BuildConfig.VERSION_FINGERPRINT
    )
}

@Preview
@Composable
fun BottomNavigationBar(
    @PreviewParameter(
        AppScreensPreviewProvider::class,
        limit = 1
    ) destinations: List<CarbonScreens>,
    navController: NavController = rememberNavController(),
    onDestinationClicked: (CarbonScreens) -> Unit = {}
) {
    NavigationBar(){
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        destinations.forEach { destination ->
            NavigationBarItem(
                selected = currentDestination
                    ?.hierarchy
                    ?.any { it.route == destination.route } == true,
                icon = {
                    Icon(
                        destination.iconData.vectorData,
                        stringResource(id = destination.iconData.iconContentDescription)
                    )
                },
                onClick = { onDestinationClicked(destination) }
            )
        }
    }
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
            CustomSnackBarContent(snackbarData.visuals.message)
        }
    )
}

@Composable
private fun CustomSnackBarContent(message: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = SnackbarDefaults.color)
            .padding(16.dp)
    ) {
        Text(
            text = message,
            color = SnackbarDefaults.actionColor
        )
    }
}

@Preview
@Composable
fun CustomSnackBarContentPreview() {
    CustomSnackBarContent(message = "Snackbar sample content")
}
