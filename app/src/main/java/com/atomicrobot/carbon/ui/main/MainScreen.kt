@file:OptIn(ExperimentalMaterialApi::class)

package com.atomicrobot.carbon.ui.main

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.data.api.github.model.Commit
import com.atomicrobot.carbon.ui.components.AtomicRobotUI
import com.atomicrobot.carbon.ui.components.BottomBar
import com.atomicrobot.carbon.util.CommitPreviewProvider
import org.koin.androidx.compose.getViewModel

@Composable
fun MainScreen(scaffoldState: ScaffoldState) {
    val viewModel: MainViewModel = getViewModel()
    val screenState by viewModel.uiState.collectAsState()
    val context: Context = LocalContext.current

    LaunchedEffect(true) {
        viewModel.fetchCommits()
        createNotificationChannel(context)
    }
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

@Preview(name = "Main Screen")
@Composable
fun MainContent(
    username: String = MainViewModel.DEFAULT_USERNAME,
    repository: String = MainViewModel.DEFAULT_REPO,
    commitsState: MainViewModel.Commits = MainViewModel.Commits.Result(emptyList()),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    onUserInputChanged: (String, String) -> Unit = { _, _ -> },
    onUserSelectedFetchCommits: () -> Unit = {},
    context: Context = LocalContext.current
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
            scaffoldState = scaffoldState,
            modifier = Modifier.weight(1f)
        )
        BottomBar()
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
    scaffoldState: ScaffoldState,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        when (commitsState) {
            is MainViewModel.Commits.Loading ->
                CircularProgressIndicator()
            is MainViewModel.Commits.Error ->
                LaunchedEffect(scaffoldState.snackbarHostState) {
                    scaffoldState.snackbarHostState.showSnackbar(message = commitsState.message)
                }
            is MainViewModel.Commits.Result -> CommitList(commits = commitsState.commits)
        }
    }
}

@Composable
fun CommitList(
    commits: List<Commit>
) {
    var clicked by remember { mutableStateOf(false)}
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(commits) { commit ->
            CommitItem(commit)
        }
    }
}

@Preview(name = "Github Commit")
@Composable
fun CommitItem(
    @PreviewParameter(CommitPreviewProvider::class, limit = 2) commit: Commit,
) {
    var clicked by remember {mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .pointerInput(Unit) {
                                detectTapGestures(
                                    onLongPress = {
                                        //can I call nav host here?
                                        //call some function here
                                        clicked = !clicked
                                    }
                                )
            },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            if(clicked) {
                //Goal is to navigate to commit details Rather than just display some text
                Text(text = "Long Pressed")
            }
            else {
                Text(
                    text = commit.commitMessage,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(text = stringResource(id = R.string.author_format, commit.author))
            }
        }
    }
}

private fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channelId = context.getString(R.string.default_notification_channel_id)
        val name = "MyNotificationChannel"
        val descriptionText = "My channel for receiving notifications"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, name, importance).apply {
            description = descriptionText
        }

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
