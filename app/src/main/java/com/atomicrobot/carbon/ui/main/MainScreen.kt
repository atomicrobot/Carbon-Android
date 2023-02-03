package com.atomicrobot.carbon.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.data.api.github.model.Commit
import com.atomicrobot.carbon.ui.components.AtomicRobotUI
import com.atomicrobot.carbon.ui.components.BottomBar
import com.atomicrobot.carbon.ui.components.BottomBarNav
import com.atomicrobot.carbon.ui.components.BottomNavigationBar
import com.atomicrobot.carbon.ui.components.NavigationTopBar
import org.koin.androidx.compose.getViewModel

//region Main Screen Composables
//endregion
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    onNavIconClicked: () -> Unit,
) {
    val viewModel: MainViewModel = getViewModel()
    val screenState by viewModel.uiState.collectAsState()

    LaunchedEffect(true) {
        viewModel.fetchCommits()
    }

    var bottomBarState by remember {
        mutableStateOf(BottomBarNav.Home)
    }

    Scaffold(
        topBar = {
            NavigationTopBar(
                title = if(bottomBarState == BottomBarNav.Home)
                    stringResource(id = R.string.carbon_home)
                else
                    stringResource(id = R.string.carbon_profile),
                navigationIcon = if(bottomBarState == BottomBarNav.Home) Icons.Filled.Menu
                else Icons.Filled.ArrowBack,
                onNavigationIconClicked = {
                    if(bottomBarState == BottomBarNav.Home)
                        onNavIconClicked()
                    else
                        bottomBarState = BottomBarNav.Home
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(
                currentTab = bottomBarState,
                onBottomTabClicked = { bottomBarState = it},
            )
        },
        modifier = modifier,
    ) {
        if(bottomBarState == BottomBarNav.Home) {
            MainContent(
                modifier = modifier.padding(it),
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
        else {
            ProfileContent(modifier = Modifier.padding(it))
        }
    }
}

@Composable
fun MainContent(
    modifier: Modifier = Modifier,
    username: String = MainViewModel.DEFAULT_USERNAME,
    repository: String = MainViewModel.DEFAULT_REPO,
    commitsState: MainViewModel.Commits = MainViewModel.Commits.Result(emptyList()),
    snackbarHostState: SnackbarHostState,
    onUserInputChanged: (String, String) -> Unit = { _, _ -> },
    onUserSelectedFetchCommits: () -> Unit = {},
    buildVersion: String,
    fingerprint: String
) {
    Column(modifier = modifier) {
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

@Composable
fun GithubUserInput(
    username: String = MainViewModel.DEFAULT_USERNAME,
    repository: String = MainViewModel.DEFAULT_REPO,
    isLoading: Boolean = false,
    onUserInputChanged: (String, String) -> Unit = { _, _ -> },
    onUserSelectedFetchCommits: () -> Unit = {}
) {
    Surface {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Username
            AtomicRobotUI.TextField.TransparentTextField(
                value = username,
                labelResId = R.string.username,
                modifier = Modifier.padding(bottom = 8.dp)
            ) { newUsername -> onUserInputChanged(newUsername, repository) }
            // Repo
            AtomicRobotUI.TextField.TransparentTextField(
                value = repository,
                labelResId = R.string.repository
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
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
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

@Composable
fun CommitItem(commit: Commit) {
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

//region Profile Composables
@Composable
fun ProfileContent(modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.carbon_profile),
            style = MaterialTheme.typography.displaySmall
        )
    }
}
//endregion

//region Composable Previews
@Preview
@Composable
fun GithubUserInputPreview() {
    GithubUserInput()
}

@Preview
@Composable
fun CommitItemPreview() {
    CommitItem(commit = dummyCommits.first())
}

@Preview
@Composable
fun CommitListPreview() {
    CommitList(dummyCommits)
}
//endregion
