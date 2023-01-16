package com.atomicrobot.carbon.ui.license

import android.text.method.LinkMovementMethod
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import android.widget.TextView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.atomicrobot.carbon.ui.theme.CarbonAndroidTheme
import io.noties.markwon.Markwon

@Composable
fun LicenseScreen(
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    val viewModel: LicenseViewModel = hiltViewModel()
    val screenState by viewModel.uiState.collectAsState()

    LaunchedEffect(true) {
        viewModel.getLicenses()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LicensesResponse(
            screenState.licensesState,
            snackbarHostState
        )
    }
}

@Composable
fun LicensesResponse(
    licensesState: LicenseViewModel.Licenses,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        when (licensesState) {
            is LicenseViewModel.Licenses.Loading ->
                CircularProgressIndicator()
            is LicenseViewModel.Licenses.Error ->
                LaunchedEffect(snackbarHostState) {
                    snackbarHostState.showSnackbar(message = licensesState.message)
                }
            is LicenseViewModel.Licenses.Result -> LicensesList(licenses = licensesState.licenses)
        }
    }
}

@Composable
fun LicensesList(licenses: String) {
    val context = LocalContext.current
    val markwon = Markwon.create(context)
    val markdown = markwon.toMarkdown(licenses)
    LazyColumn(contentPadding = PaddingValues(top = 8.dp, start = 16.dp, end = 16.dp)) {
        item {
            AndroidView(factory = {
                TextView(it).apply {
                    layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                    movementMethod = LinkMovementMethod.getInstance()
                }
            }, update = {
                it.text = markdown
            })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LicenseScreenPreview() {
    CarbonAndroidTheme {
        LicenseScreen()
    }
}
