package com.atomicrobot.carbon.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.data.api.github.model.Commit
import com.atomicrobot.carbon.ui.components.BottomBar
import com.atomicrobot.carbon.ui.components.TopBar
import com.atomicrobot.carbon.ui.components.TransparentTextField

@Composable
fun Main(mainViewModelCompose: MainViewModelCompose) {

    val scaffoldState: ScaffoldState = rememberScaffoldState()

    val mainState by mainViewModelCompose.uiState.collectAsState()

    MainContent(
        userName = mainState.username,
        repository = mainState.repository,
        commitsState = mainState.commitsState,
        scaffoldState = scaffoldState,
        onUserInputChanged = { username, repo ->
            mainViewModelCompose.updateUserInput(username, repo)
        },
        onFetchCommitsClick = { mainViewModelCompose.fetchCommits() },
    )
}

@Composable
fun MainContent(
    userName: String,
    repository: String,
    commitsState: MainViewModelCompose.CommitsState,
    scaffoldState: ScaffoldState,
    onUserInputChanged: (String, String) -> Unit,
    onFetchCommitsClick: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopBar()
            GithubInput(
                userName = userName,
                repository = repository,
                isLoading = commitsState is MainViewModelCompose.CommitsState.Loading,
                onUserInputChanged = onUserInputChanged,
                onFetchCommitsClick = onFetchCommitsClick
            )
            GitHubResponse(
                commitsState = commitsState,
                scaffoldState = scaffoldState,
                modifier = Modifier.weight(1f)
            )
            BottomBar()
        }
    }
}

@Composable
fun GithubInput(
    userName: String,
    repository: String,
    isLoading: Boolean,
    onUserInputChanged: (String, String) -> Unit,
    onFetchCommitsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colors.onSurface
            .copy(alpha = TextFieldDefaults.BackgroundOpacity)
    ) {
        Column(modifier = modifier
            .fillMaxWidth()
            .padding(16.dp))
        {
            TransparentTextField(
                value = userName,
                labelResId = R.string.repository,
                modifier = Modifier.padding(bottom = 8.dp)
            ) { onUserInputChanged(it, repository) }
            TransparentTextField(
                value = repository,
                labelResId = R.string.repository,
            ) { onUserInputChanged(userName, it) }
            OutlinedButton(
                onClick = onFetchCommitsClick,
                enabled = !isLoading && userName.isNotEmpty() && repository.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.fetch_commits))
            }
        }
    }
}

@Composable
fun GitHubResponse(
    commitsState: MainViewModelCompose.CommitsState,
    scaffoldState: ScaffoldState,
    modifier: Modifier
) {
    Box(modifier = modifier) {
        when (commitsState) {
            is MainViewModelCompose.CommitsState.Error ->
                Error(
                    text = commitsState.message,
                    scaffoldState = scaffoldState
                )
            MainViewModelCompose.CommitsState.Loading -> LoadingCommits()
            is MainViewModelCompose.CommitsState.Result ->
                CommitList(commits = commitsState.commits)
        }
    }
}

@Composable
fun Error(text: String, scaffoldState: ScaffoldState) {
    LaunchedEffect(scaffoldState.snackbarHostState) {
        scaffoldState.snackbarHostState.showSnackbar(text)
    }
}

@Composable
fun LoadingCommits() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun CommitList(commits: List<Commit>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(commits) {
            CommitUiElement(commit = it)
        }
    }
}

@Composable
fun CommitUiElement(commit: Commit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = commit.commitMessage,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(text = stringResource(id = R.string.author_format, commit.author))
        }
    }
}
