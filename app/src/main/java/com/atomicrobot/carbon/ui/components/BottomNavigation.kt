package com.atomicrobot.carbon.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.atomicrobot.carbon.navigation.CarbonScreens
import com.atomicrobot.carbon.navigation.LumenScreens
import com.atomicrobot.carbon.ui.compose.AppScreensPreviewProvider
import com.atomicrobot.carbon.ui.compose.LumenScreensPreviewProvider
import com.atomicrobot.carbon.ui.shader.AngledLinearGradient
import com.atomicrobot.carbon.ui.theme.LightBlurple
import com.atomicrobot.carbon.ui.theme.White15
import com.atomicrobot.carbon.ui.theme.White25
import com.atomicrobot.carbon.ui.theme.White3

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
                        destination.iconData.vectorData,
                        stringResource(id = destination.iconData.iconContentDescription)
                    )
                },
                onClick = { onDestinationClicked(destination) }
            )
        }
    }
}

@Preview
@Composable
fun LumenBottomNavigationBar(
    @PreviewParameter(
        LumenScreensPreviewProvider::class,
        limit = 1
    ) destinations: List<LumenScreens>,
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
    onDestinationClicked: (LumenScreens) -> Unit = {}
) {
    BottomNavigation(modifier = modifier) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        destinations.forEach { destination ->

            val selected = currentDestination
                ?.hierarchy
                ?.any { it.route == destination.route } == true

            BottomNavigationItem(
                selected = selected,
                icon = {
                    // Tweak the background if the navigation item is currently selected
                    val backgroundMod =
                        if (selected)
                            Modifier
                                .clip(CircleShape)
                                .background(LightBlurple)
                                .border(
                                    width = 2.dp,
                                    brush = AngledLinearGradient(
                                        colors = listOf(White25, White15, White3),
                                        angleInDegrees = 0F,
                                        useAsCssAngle = false
                                    ),
                                    CircleShape
                                )
                        else
                            Modifier.clip(CircleShape)
                                .background(Color.Transparent)
                    Image(
                        painterResource(id = destination.iconResourceId),
                        stringResource(id = destination.iconContentDescription),
                        modifier = Modifier
                            .size(48.dp)
                            .then(backgroundMod)
                            .padding(
                                horizontal = 13.67.dp,
                                vertical = 14.dp
                            )
                    )
                },
                onClick = { onDestinationClicked(destination) }
            )
        }
    }
}
