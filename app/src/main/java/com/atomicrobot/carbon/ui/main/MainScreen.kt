package com.atomicrobot.carbon.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.atomicrobot.carbon.BuildConfig
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.data.api.github.model.Commit
import com.atomicrobot.carbon.ui.components.AtomicRobotUI
import com.atomicrobot.carbon.ui.components.BottomBar
import com.atomicrobot.carbon.util.CommitPreviewProvider

@Composable
fun MainScreen(snackbarHostState: SnackbarHostState) {
    val viewModel: MainViewModel = hiltViewModel()
    val screenState by viewModel.uiState.collectAsState()

    LaunchedEffect(true) {
        viewModel.fetchCommits()
    }
    MainContent(
        username = screenState.username,
        repository = screenState.repository,
        commitsState = screenState.commitsState,
        snackbarHostState = snackbarHostState,
        onUserInputChanged = { username, repository ->
            viewModel.updateUserInput(username, repository)
        },
        onUserSelectedFetchCommits = {
            viewModel.fetchCommits()
        },
        buildVersion = viewModel.getVersion(),
        fingerprint = viewModel.getVersionFingerprint()
    )
}

@Composable
fun MainContent(
    username: String = MainViewModel.DEFAULT_USERNAME,
    repository: String = MainViewModel.DEFAULT_REPO,
    commitsState: MainViewModel.Commits = MainViewModel.Commits.Result(emptyList()),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onUserInputChanged: (String, String) -> Unit = { _, _ -> },
    onUserSelectedFetchCommits: () -> Unit = {},
    buildVersion: String,
    fingerprint: String
) {
    Column {
        /*
        * Main Screen is split into three chunks
        * (1) User Input
        *       user name Textfield
        *       repo Textfield
        *       fetch commits button
        * (2) Commit List / Circular Progress when loading
        * (3) App Info Bottom Bar
        */
        GithubUserInput(
            username = username,
            repository = repository,
            isLoading = false,
            onUserInputChanged = onUserInputChanged,
            onUserSelectedFetchCommits = onUserSelectedFetchCommits
        )
        GithubResponse(
            commitsState = commitsState,
            snackbarHostState = snackbarHostState,
            modifier = Modifier.weight(1f)
        )
        BottomBar(
            buildVersion = buildVersion,
            fingerprint = fingerprint
        )
    }
}

@Preview(name = "Main Screen")
@Composable
fun MainContentPreview(
    username: String = MainViewModel.DEFAULT_USERNAME,
    repository: String = MainViewModel.DEFAULT_REPO,
    commitsState: MainViewModel.Commits = MainViewModel.Commits.Result(emptyList()),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onUserInputChanged: (String, String) -> Unit = { _, _ -> },
    onUserSelectedFetchCommits: () -> Unit = {},
    buildVersion: String = BuildConfig.VERSION_NAME,
    fingerprint: String = BuildConfig.VERSION_FINGERPRINT
) {
    MainContent(
        buildVersion = buildVersion,
        fingerprint = fingerprint
    )
}

@Preview(name = "User Input")
@Composable
fun GithubUserInput(
    username: String = MainViewModel.DEFAULT_USERNAME,
    repository: String = MainViewModel.DEFAULT_REPO,
    isLoading: Boolean = false,
    onUserInputChanged: (String, String) -> Unit = { _, _ -> },
    onUserSelectedFetchCommits: () -> Unit = {}
) {
    Surface(
        //TODO not sure what replaces this BackgroundOpacity value
        color = MaterialTheme.colorScheme.primaryContainer/*.copy(
            alpha = TextFieldDefaults.BackgroundOpacity
        )*/
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Username
            AtomicRobotUI.TextField.TransparentTextField(
                value = username,
                label = stringResource(id = R.string.username),
                modifier = Modifier.padding(bottom = 8.dp)
            ) { newUsername -> onUserInputChanged(newUsername, repository) }
            // Repo
            AtomicRobotUI.TextField.TransparentTextField(
                value = repository,
                label = stringResource(id = R.string.repository)
            ) { newRepo -> onUserInputChanged(username, newRepo) }
            // Fetch commits
            AtomicRobotUI.Button.Outlined(
                text = stringResource(id = R.string.fetch_commits),
                onClick = onUserSelectedFetchCommits,
                // Make sure the button is disabled when loading or the input fields are empty
                enabled = !isLoading && (username.isNotEmpty() && repository.isNotEmpty()),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun GithubResponse(
    commitsState: MainViewModel.Commits,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        when (commitsState) {
            is MainViewModel.Commits.Loading ->
                CircularProgressIndicator()
            is MainViewModel.Commits.Error ->
                LaunchedEffect(snackbarHostState) {
                    snackbarHostState.showSnackbar(message = commitsState.message)
                }
            is MainViewModel.Commits.Result -> CommitList(commits = commitsState.commits)
        }
    }
}

@Composable
fun CommitList(commits: List<Commit>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(commits) { commit ->
            CommitItem(commit)
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
                modifier = Modifier.padding(bottom = 8.dp),
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = stringResource(id = R.string.author_format, commit.author),
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}
