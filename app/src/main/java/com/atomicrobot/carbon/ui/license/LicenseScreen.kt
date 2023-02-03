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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.atomicrobot.carbon.navigation.CarbonScreens
import com.atomicrobot.carbon.ui.components.NavigationTopBar
import com.atomicrobot.carbon.ui.theme.CarbonAndroidTheme
import io.noties.markwon.Markwon
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LicenseScreen(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    onNavIconClicked: () -> Unit,
) {
    val viewModel: LicenseViewModel = getViewModel()
    val screenState by viewModel.uiState.collectAsState()

    LaunchedEffect(true) {
        viewModel.getLicenses()
    }

    Scaffold(
        topBar = {
            NavigationTopBar(
                title = stringResource(id = CarbonScreens.License.title),
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
            LicensesResponse(
                screenState.licensesState,
                snackbarHostState
            )
        }
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
        val snackbarHostState = remember { SnackbarHostState() }
        LicenseScreen(
            snackbarHostState = snackbarHostState,
            onNavIconClicked = {}
        )
    }
}
