package com.atomicrobot.carbon.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.data.api.github.model.Commit
import com.atomicrobot.carbon.ui.components.BottomBar
import com.atomicrobot.carbon.ui.components.CustomSnackbar
import com.atomicrobot.carbon.ui.components.TopBar
import com.atomicrobot.carbon.ui.components.TransparentTextField
import com.atomicrobot.carbon.ui.compose.CommitPreviewProvider

@Composable
fun MainScreen() {
    val viewModel: MainViewModel = hiltViewModel()
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val viewState by viewModel.viewState.collectAsState()

    LaunchedEffect(true) {
        viewModel.fetchCommits()
    }

    MainContent(
        userName = viewState.username,
        repository = viewState.repository,
        commitsState = viewState.commitsState,
        scaffoldState = scaffoldState,
        onUserInputChanged = { username, repo ->
            viewModel.updateUserInput(username, repo)
        },
        onFetchCommitsClick = { viewModel.fetchCommits() },
    )
}

@Preview(name = "Main Screen")
@Composable
fun MainContent(
    userName: String = MainViewModel.DEFAULT_USERNAME,
    repository: String = MainViewModel.DEFAULT_REPO,
    commitsState: MainViewModel.Commits = MainViewModel.Commits.Result(emptyList()),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    onUserInputChanged: (String, String) -> Unit = { _, _ -> },
    onFetchCommitsClick: () -> Unit = {},
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        scaffoldState = scaffoldState,
        snackbarHost = { CustomSnackbar(hostState = scaffoldState.snackbarHostState) }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopBar()
            GithubUserInput(
                userName = userName,
                repository = repository,
                isLoading = commitsState is MainViewModel.Commits.Loading,
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

@Preview(name = "User Input")
@Composable
fun GithubUserInput(
    userName: String = MainViewModel.DEFAULT_USERNAME,
    repository: String = MainViewModel.DEFAULT_REPO,
    isLoading: Boolean = false,
    onUserInputChanged: (String, String) -> Unit = { _, _ -> },
    onFetchCommitsClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colors.onSurface
            .copy(alpha = TextFieldDefaults.BackgroundOpacity)
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
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
                enabled = !isLoading && (userName.isNotEmpty() && repository.isNotEmpty()),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.fetch_commits))
            }
        }
    }
}

@Composable
fun GitHubResponse(
    commitsState: MainViewModel.Commits,
    scaffoldState: ScaffoldState,
    modifier: Modifier
) {
    Box(modifier = modifier) {
        when (commitsState) {
            is MainViewModel.Commits.Error ->
                Error(
                    text = commitsState.message,
                    scaffoldState = scaffoldState
                )
            MainViewModel.Commits.Loading -> LoadingCommits()
            is MainViewModel.Commits.Result ->
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
            CommitItem(commit = it)
        }
    }
}

@Preview(name = "Github Commit")
@Composable
fun CommitItem(@PreviewParameter(CommitPreviewProvider::class, limit = 2) commit: Commit) {
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
