package com.atomicrobot.carbon.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.atomicrobot.carbon.ui.navigation.AppBarState

@Composable
fun CarbonScreen(
    onUpdateAppBarState: (AppBarState) -> Unit,
    onDrawerClicked: () -> Unit,
    title: String,
    content: @Composable () -> Unit
) {

    LaunchedEffect(Unit){
        onUpdateAppBarState(
            AppBarState.defaultAppBarState(
                title = title,
                onDrawerClicked = onDrawerClicked
            )
        )
    }

    content()

}