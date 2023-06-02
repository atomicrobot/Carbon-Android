package com.atomicrobot.carbon.ui.clickableCards

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.atomicrobot.carbon.data.api.github.model.DetailedCommit
import com.atomicrobot.carbon.util.DetailedCommitPreviewProvider
import org.koin.androidx.compose.getViewModel


@Preview
@Composable
fun GitInfoScreen(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
) {
    val viewModel: GitCardInfoViewModel = getViewModel()
    val screenState by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = true) {
        viewModel.fetchDetailedCommit()
    }
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val navBackStackEntry: NavBackStackEntry? by navController.currentBackStackEntryAsState()
    Scaffold {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Test Text")
            DetailedGitInfoResponse(
                detailedCommitState = screenState.detailedCommitState,
                scaffoldState = scaffoldState,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun DetailedGitInfoResponse(
    detailedCommitState: GitCardInfoViewModel.DetailedCommit,
    scaffoldState: ScaffoldState,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        when (detailedCommitState) {
            is GitCardInfoViewModel.DetailedCommit.Loading ->
                CircularProgressIndicator()
            is GitCardInfoViewModel.DetailedCommit.Error ->
                LaunchedEffect(scaffoldState.snackbarHostState) {
                    scaffoldState.snackbarHostState.showSnackbar(message = detailedCommitState.message)
                }
            is GitCardInfoViewModel.DetailedCommit.Result -> Details(details = detailedCommitState.commit)
        }
    }
}

/*Hopefully renders A lazy column of the info in the detailed commit but preview is not working*/
@Composable
fun Details(details: List<DetailedCommit>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(details) { item ->
                TextBoxForDetail(detailedCommit = item)
        }
    }
}

@Composable
fun TextBoxForDetail(
    @PreviewParameter(DetailedCommitPreviewProvider::class, limit = 1) detailedCommit: DetailedCommit
) {
    Text(text = detailedCommit.detailedCommitAuthor)
}

