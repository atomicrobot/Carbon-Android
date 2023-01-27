package com.atomicrobot.carbon.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
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

@Composable
fun CarbonTopBarActions() = Icon(
    painter = painterResource(id = R.drawable.carbon_android_logo),
    contentDescription = stringResource(id = R.string.cont_desc_shell),
    modifier = Modifier
        .padding(end = 12.dp)
        .size(24.dp),
    tint = Color.Unspecified
)

@Composable
fun CarbonTopBarNavigation(
    onDrawerClicked: () -> Unit
) = IconButton(
    onClick = onDrawerClicked,
    content = {
        Icon(imageVector = Icons.Filled.Menu, contentDescription = "Menu Drawer")
    }
)

@Composable
fun DesignSystemDetailNavigation(
    onBackToSystemClicked: () -> Unit
) = IconButton(
    onClick = onBackToSystemClicked,
    content = {
        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
    }
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DesignSystemTopBarActions(
    onDarkModeToggled: (Boolean) -> Unit,
    onFontScaleSet: (Float) -> Unit,
    fontScale: Float,
    darkMode: Boolean
){

    var expandedMenu by remember { mutableStateOf(false) }

    Box(modifier = Modifier.wrapContentSize()) {
        Button(
            onClick = { expandedMenu = !expandedMenu },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            shape = RectangleShape,
            contentPadding = PaddingValues(
                start = 12.dp,
                top = 8.dp,
                bottom = 8.dp,
                end = 0.dp
            ),
            content = {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${fontScale}x",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Icon(
                        imageVector = if (expandedMenu) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                        contentDescription = "dropdown menu",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        )

        DropdownMenu(
            modifier = Modifier.background(color = MaterialTheme.colorScheme.surface),
            expanded = expandedMenu,
            onDismissRequest = { expandedMenu = false }
        ) {
            listOf(0.75f, 1.0f, 1.5f, 2.0f).forEach {
                DropdownMenuItem(
                    text = { Text(text = "${it}x") },
                    colors = MenuDefaults.itemColors(
                        textColor = MaterialTheme.colorScheme.onSurface
                    ),
                    onClick = {
                        onFontScaleSet(it)
                        expandedMenu = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }

    IconToggleButton(
        checked = darkMode,
        onCheckedChange = onDarkModeToggled
    ) {
        Icon(
            imageVector = if (darkMode) Icons.Filled.DarkMode else Icons.Filled.LightMode,
            contentDescription = if (darkMode) "Enable Light mode" else "Enable Dark mode"
        )
    }
}

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    buildVersion: String,
    fingerprint: String
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primaryContainer)
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
    NavigationBar(
        modifier = Modifier.height(64.dp),
        tonalElevation = 0.dp
    ) {
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
