package com.atomicrobot.carbon.ui.designSystems

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.navigation.appScreens
import com.atomicrobot.carbon.ui.components.*

@Composable
fun getBarComposables(): List<@Composable () -> Unit> {

    return listOf(
        { BarComposableContentItem(section = AppBarType.TOP_BAR) },
        { BarComposableContentItem(section = AppBarType.CARBON_BOTTOM_BAR) },
        { BarComposableContentItem(section = AppBarType.CARBON_BOTTOM_NAVIGATION_BAR) },
        { BarComposableContentItem(section = AppBarType.CARBON_SNACK_BAR) }
    )
}

@Composable
fun BarComposableContentItem(
    section: AppBarType
) {

    Column(
        modifier = Modifier
            .padding(vertical = 16.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = section.sectionTitle,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        SelectedBar(section)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectedBar(
    section: AppBarType
){
    when (section){
        AppBarType.TOP_BAR -> {
            TopAppBar(
                title = { Text(text = "Home") },
                navigationIcon = { CarbonTopBarNavigation(
                    onDrawerClicked = {}
                ) },
                actions = { CarbonTopBarActions() }
            )
        }
        AppBarType.CARBON_BOTTOM_BAR -> {
            BottomBar(
                buildVersion = "1.0.0",
                fingerprint = "DEV"
            )
        }
        AppBarType.CARBON_BOTTOM_NAVIGATION_BAR -> {
            BottomNavigationBar(destinations = appScreens)
        }
        AppBarType.CARBON_SNACK_BAR -> {
            val snackBarHostState = remember { SnackbarHostState() }

            CustomSnackbar(hostState = snackBarHostState)

            LaunchedEffect(Unit){
                snackBarHostState.showSnackbar(
                    message = "Design System",
                    duration = SnackbarDuration.Indefinite
                )
            }
        }
    }
}

enum class AppBarType(
    val sectionTitle: String
) {
    TOP_BAR(
        sectionTitle = "Top App Bar"
    ),
    CARBON_BOTTOM_BAR(
        sectionTitle = "Carbon Bottom Bar"
    ),
    CARBON_BOTTOM_NAVIGATION_BAR(
        sectionTitle = "Carbon Bottom Navigation Bar"
    ),
    CARBON_SNACK_BAR(
        sectionTitle = "Carbon Snack Bar"
    )
}