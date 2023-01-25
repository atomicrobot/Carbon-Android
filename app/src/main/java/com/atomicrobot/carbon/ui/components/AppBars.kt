package com.atomicrobot.carbon.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.SnackbarDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.atomicrobot.carbon.BuildConfig
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.navigation.CarbonScreens
import com.atomicrobot.carbon.navigation.appScreens
import com.atomicrobot.carbon.ui.theme.CarbonAndroidTheme

//region Composables
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationTopBar(
    title: String = CarbonScreens.Home.title,
    navigationIcon: ImageVector = Icons.Filled.Menu,
    onNavigationIconClicked: () -> Unit = {}
) = TopAppBar(
    title = {
        Text(text = title)
    },
    navigationIcon = {
        IconButton(onClick = onNavigationIconClicked) {
            Icon(imageVector = navigationIcon, contentDescription = navigationIcon.name)
        }
    },
    actions = {
        Icon(
            painter = painterResource(id = R.drawable.carbon_android_logo),
            contentDescription = stringResource(id = R.string.cont_desc_shell),
            modifier = Modifier
                .size(48.dp)
                .padding(12.dp),
            tint = Color.Unspecified,
        )
    },
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(title: String = "") = TopAppBar(title = { Text(text = title) })

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    buildVersion: String,
    fingerprint: String
) {
    Surface(
        modifier = modifier
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(modifier.padding(16.dp)) {
            Text(
                text = stringResource(id = R.string.version_format, buildVersion),
                modifier = Modifier.weight(1f)
            )
            Text(
                text = stringResource(id = R.string.fingerprint_format, fingerprint)
            )
        }
    }
}

@Composable
fun BottomNavigationBar(
    destinations: List<CarbonScreens>,
    currentDestination: NavDestination?,
    onDestinationClicked: (CarbonScreens) -> Unit = {}
) {
    NavigationBar {
        destinations.forEach { destination ->
            val selected = currentDestination
                ?.hierarchy
                ?.any { it.route == destination.route } == true
            NavigationBarItem(
                selected = selected,
                icon = {
                    Icon(
                        if (selected) destination.iconData.selectedIcon
                        else destination.iconData.unselectedIcon,
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
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    SnackbarHost(
        modifier = modifier.fillMaxWidth(),
        hostState = snackbarHostState
    ) { CustomSnackBarContent(it.visuals.message) }
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
            color = SnackbarDefaults.contentColor
        )
    }
}
// endRegion

//region Previews
@Preview
@Composable
fun NavigationTopBarPreview() {
    CarbonAndroidTheme {
        NavigationTopBar()
    }
}

@Preview
@Composable
fun BottomBarPreview() {
    CarbonAndroidTheme {
        BottomBar(
            buildVersion = BuildConfig.VERSION_NAME,
            fingerprint = BuildConfig.VERSION_FINGERPRINT
        )
    }
}

@Preview
@Composable
fun BottomNavigationBarPreview() {
    CarbonAndroidTheme {
        BottomNavigationBar(appScreens, null)
    }
}

@Preview
@Composable
fun CustomSnackBarContentPreview() {
    CarbonAndroidTheme {
        CustomSnackBarContent(message = "Snackbar sample content")
    }
}
//endregion
