package com.atomicrobot.carbon.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.atomicrobot.carbon.navigation.CarbonScreens
import com.atomicrobot.carbon.ui.components.NavigationTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onNavIconClicked: () -> Unit,
) {
    Scaffold(
        topBar = {
            NavigationTopBar(
                title = stringResource(id = CarbonScreens.Settings.title),
                navigationIcon = Icons.Filled.ArrowBack,
                onNavigationIconClicked = onNavIconClicked
            )
        },
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Settings", style = MaterialTheme.typography.displaySmall)
        }
    }
}
