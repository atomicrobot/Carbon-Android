package com.atomicrobot.carbon.ui.main

import androidx.compose.foundation.background
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
import com.atomicrobot.carbon.BuildConfig
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.data.api.github.model.Author
import com.atomicrobot.carbon.data.api.github.model.Commit
import com.atomicrobot.carbon.data.api.github.model.CommitDetails
import com.atomicrobot.carbon.ui.theme.CarbonAndroidTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.pow

@Composable
fun Main(mainViewModelCompose: MainViewModelCompose) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val mainState by mainViewModelCompose.uiState.collectAsState()

    MainContent(
        scope = scope,
        userName = mainState.username,
        repository = mainState.repository,
        commitsState = mainState.commitsState,
        snackbarHostState = snackbarHostState,
        onUserInputChanged = { username, repo ->
            mainViewModelCompose.updateUserInput(username, repo)
        },
        onFetchCommitsClick = { mainViewModelCompose.fetchCommits() },
    )
}

@Composable
fun MainContent(
    scope: CoroutineScope,
    userName: String,
    repository: String,
    commitsState: MainViewModelCompose.CommitsState,
    snackbarHostState: SnackbarHostState,
    onUserInputChanged: (String, String) -> Unit,
    onFetchCommitsClick: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(text = BuildConfig.APPLICATION_ID) }
        )
        GithubInput(
            userName = userName,
            repository = repository,
            isLoading = commitsState is MainViewModelCompose.CommitsState.Loading,
            onUserInputChanged = onUserInputChanged,
            onFetchCommitsClick = onFetchCommitsClick
        )
        GitHubResponse(
            scope = scope,
            commitsState = commitsState,
            snackbarHostState = snackbarHostState,
            modifier = Modifier.weight(1f)
        )
        SnackbarHost(hostState = snackbarHostState)
        BottomBar()
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
            .padding(16.dp)) {
            TextField(
                value = userName,
                onValueChange = { onUserInputChanged(it, repository) },
                label = {
                    Text(text = stringResource(id = R.string.username))
                },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = repository,
                onValueChange = { onUserInputChanged(userName, it) },
                label = {
                    Text(text = stringResource(id = R.string.repository))
                },
                modifier = Modifier.fillMaxWidth()
            )
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
    scope: CoroutineScope,
    commitsState: MainViewModelCompose.CommitsState,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier
) {
    when (commitsState) {
        is MainViewModelCompose.CommitsState.Error -> Error(scope, snackbarHostState, modifier)
        MainViewModelCompose.CommitsState.Loading -> LoadingCommits(modifier)
        is MainViewModelCompose.CommitsState.Result -> CommitList(commitsState.commits, modifier)
    }
}

@Composable
fun Error(scope: CoroutineScope, snackbarHostState: SnackbarHostState, modifier: Modifier) {
    Column(modifier = modifier) {
        LaunchedEffect(Unit) {
            scope.launch {
                snackbarHostState.showSnackbar("Hello there")
            }
        }
    }
}

@Composable
fun LoadingCommits(modifier: Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun CommitList(commits: List<Commit>, modifier: Modifier) {
    LazyColumn(modifier = modifier){
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

@Composable
fun BottomBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colors.onSurface
                    .copy(alpha = TextFieldDefaults.BackgroundOpacity)
            )
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp)
    ) {
        Text(
            text = stringResource(
                id = R.string.version_format, BuildConfig.VERSION_NAME
            ),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = stringResource(
                id = R.string.fingerprint_format, BuildConfig.VERSION_FINGERPRINT
            )
        )
    }
}
