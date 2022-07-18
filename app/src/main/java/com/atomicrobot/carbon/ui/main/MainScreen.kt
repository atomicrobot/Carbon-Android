package com.atomicrobot.carbon.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
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
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.data.api.github.model.Commit
import com.atomicrobot.carbon.navigation.TopBar
import com.atomicrobot.carbon.ui.components.BottomBar
import com.atomicrobot.carbon.ui.components.CustomSnackbar
import com.atomicrobot.carbon.ui.components.TransparentTextField
import com.atomicrobot.carbon.ui.compose.CommitPreviewProvider
import org.koin.androidx.compose.getViewModel

@Composable
fun MainScreen(openDrawer: () -> Unit) {
    val viewModel: MainViewModel = getViewModel()
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val screenState by viewModel.uiState.collectAsState()

    LaunchedEffect(true) {
        viewModel.fetchCommits()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopBar(
            title = "Home",
            buttonIcon = Icons.Filled.Menu,
            onButtonClicked = { openDrawer() }
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            MainContent(
                username = screenState.username,
                repository = screenState.repository,
                commitsState = screenState.commitsState,
                scaffoldState = scaffoldState,
                onUserInputChanged = { username, repository ->
                    viewModel.updateUserInput(username, repository)
                },
                onUserSelectedFetchCommits = {
                    viewModel.fetchCommits()
                }
            )
        }
    }
}

@Preview(name = "Main Screen")
@Composable
fun MainContent(
    username: String = MainViewModel.DEFAULT_USERNAME,
    repository: String = MainViewModel.DEFAULT_REPO,
    commitsState: MainViewModel.Commits = MainViewModel.Commits.Result(emptyList()),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    onUserInputChanged: (String, String) -> Unit = { _, _ -> },
    onUserSelectedFetchCommits: () -> Unit = {}
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        scaffoldState = scaffoldState,
        bottomBar = { BottomBar() },
        snackbarHost = { CustomSnackbar(hostState = scaffoldState.snackbarHostState) }
    ) { padding -> /* Must use padding arg otherwise causes compiler issue */
        Column(modifier = Modifier.padding(padding)) {
            /*
            * Main Screen is split into two chunks
            * (1) User Input
            *       user name Textfield
            *       repo Textfield
            *       fetch commits button
            * (2) Commit List / Circular Progress when loading
            */
            GithubUserInput(
                username = username,
                repository = repository,
                isLoading = false,
                onUserInputChanged = onUserInputChanged,
                onUserSelectedFetchCommits = onUserSelectedFetchCommits
            )
            GithubResponse(commitsState = commitsState, scaffoldState = scaffoldState)
        }
    }
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
        color = MaterialTheme.colors.onSurface.copy(
            alpha = TextFieldDefaults.BackgroundOpacity
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Username
            TransparentTextField(
                value = username,
                labelResId = R.string.username,
                modifier = Modifier.padding(bottom = 8.dp)
            ) { newUsername -> onUserInputChanged(newUsername, repository) }
            // Repo
            TransparentTextField(
                value = repository,
                labelResId = R.string.repository
            ) { newRepo -> onUserInputChanged(username, newRepo) }
            // Fetch commits
            OutlinedButton(
                onClick = onUserSelectedFetchCommits,
                // Make sure the button is disabled when loading or the input fields are empty
                enabled = !isLoading && (username.isNotEmpty() && repository.isNotEmpty()),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.fetch_commits))
            }
        }
    }
}

@Composable
fun GithubResponse(
    commitsState: MainViewModel.Commits,
    scaffoldState: ScaffoldState,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        when (commitsState) {
            is MainViewModel.Commits.Loading ->
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            is MainViewModel.Commits.Error ->
                LaunchedEffect(scaffoldState.snackbarHostState) {
                    scaffoldState.snackbarHostState.showSnackbar(message = commitsState.message)
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
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(text = stringResource(id = R.string.author_format, commit.author))
        }
    }
}
