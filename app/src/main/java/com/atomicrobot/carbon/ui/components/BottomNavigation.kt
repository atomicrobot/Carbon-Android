package com.atomicrobot.carbon.ui.components

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.atomicrobot.carbon.navigation.AppScreens

@Composable
fun BottomNavigationBar(
    navController: NavController,
    destinations: List<AppScreens>,
    onDestinationClicked: (AppScreens) -> Unit) {
    BottomNavigation {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        destinations.forEach { destination ->
            BottomNavigationItem(
                selected = currentDestination
                    ?.hierarchy
                    ?.any { it.route == destination.route} == true,
                icon = { Icon(
                    destination.iconData.icon,
                    stringResource(id = destination.iconData.iconContentDescription)
                ) },
                onClick = {
                    onDestinationClicked(destination)
                })
            }
    }
}