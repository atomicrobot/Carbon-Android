package com.atomicrobot.carbon.ui.clickableCards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Checkbox
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.atomicrobot.carbon.data.api.github.model.DetailedCommit
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
    Scaffold { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
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
    detailedCommitState: GitCardInfoViewModel.GitHubResponse,
    scaffoldState: ScaffoldState,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        when (detailedCommitState) {
            is GitCardInfoViewModel.GitHubResponse.Loading ->
                CircularProgressIndicator()
            is GitCardInfoViewModel.GitHubResponse.Error ->
                LaunchedEffect(scaffoldState.snackbarHostState) {
                    scaffoldState.snackbarHostState.showSnackbar(message = detailedCommitState.message)
                }
            is GitCardInfoViewModel.GitHubResponse.Result -> Details(details = detailedCommitState.commit)
        }
    }
}

/*Hopefully renders A lazy column of the info in the detailed commit but preview is not working*/
@Composable
fun Details(details: DetailedCommit?) {
//    LazyColumn(modifier = Modifier.fillMaxSize()) {
//        items(details) {commit ->
//            TextBoxForDetail(detailedCommit = commit)
//        }
//    }
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
        .verticalScroll(rememberScrollState())
    ) {
        if (details?.detailedCommitMessage != null) {
            Row() {
                Text(text = "Author: ", fontWeight = FontWeight.Bold)
                Text(details.detailedCommitAuthor)
            }
            Text(text = "Message: ", fontWeight = FontWeight.Bold)
            Text(text = details.detailedCommitMessage)
            Text(text = "TreeUrl :", fontWeight = FontWeight.Bold)
            Text(text = details.detailedCommitTreeURL)
            Row() {
                Text(text = "Verified: ", fontWeight = FontWeight.Bold)
                Checkbox(checked = details.detailedCommitVerified, onCheckedChange = null)
            }
        } else {
            Text(text = "An error has occurred retrieving or displaying this commit")
        }
    }
}

