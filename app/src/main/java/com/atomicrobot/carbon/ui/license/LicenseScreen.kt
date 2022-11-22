package com.atomicrobot.carbon.ui.license

import android.text.method.LinkMovementMethod
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import android.widget.TextView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.atomicrobot.carbon.ui.theme.CarbonAndroidTheme
import io.noties.markwon.Markwon
import org.koin.androidx.compose.getViewModel

@Composable
fun LicenseScreen(
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    val viewModel: LicenseViewModel = getViewModel()
    val screenState by viewModel.uiState.collectAsState()

    LaunchedEffect(true) {
        viewModel.getLicenses()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LicensesResponse(
            screenState.licensesState,
            scaffoldState
        )
    }
}

@Composable
fun LicensesResponse(
    licensesState: LicenseViewModel.Licenses,
    scaffoldState: ScaffoldState,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        when (licensesState) {
            is LicenseViewModel.Licenses.Loading ->
                CircularProgressIndicator()
            is LicenseViewModel.Licenses.Error ->
                LaunchedEffect(scaffoldState.snackbarHostState) {
                    scaffoldState.snackbarHostState.showSnackbar(message = licensesState.message)
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
    LazyColumn {
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
