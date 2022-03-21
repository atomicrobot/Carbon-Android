package com.atomicrobot.carbon.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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
    Row(modifier = Modifier
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