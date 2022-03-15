package com.atomicrobot.carbon.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.data.api.github.model.Author
import com.atomicrobot.carbon.data.api.github.model.Commit
import com.atomicrobot.carbon.data.api.github.model.CommitDetails

@Composable
fun Main(mainViewModelCompose: MainViewModelCompose) {
    val mainState by mainViewModelCompose.uiState.collectAsState()
    MainContent(
        userName = mainState.state.username,
        repository = mainState.state.repository,
        commits = mainState.commits,
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
    commits: MainViewModelCompose.Commits,
    onUserInputChanged: (String, String) -> Unit,
    onFetchCommitsClick: () -> Unit,
) {
    Column {
        GithubInput(
            userName = userName,
            repository = repository,
            onUserInputChanged = onUserInputChanged,
            onFetchCommitsClick = onFetchCommitsClick
        )
        GitHubResponse(commits = commits)
    }
}

@Composable
fun GithubInput(
    userName: String,
    repository: String,
    onUserInputChanged: (String, String) -> Unit,
    onFetchCommitsClick: () -> Unit
) {
    Surface {
        Column {
            TextField(
                value = userName,
                onValueChange = { onUserInputChanged(it, repository) },
                label = {
                    Text(text = stringResource(id = R.string.username))
                }
            )
            TextField(
                value = repository,
                onValueChange = { onUserInputChanged(userName, it) },
                label = {
                    Text(text = stringResource(id = R.string.repository))
                }
            )
            OutlinedButton(
                onClick = onFetchCommitsClick
            ) {
                Text(text = stringResource(id = R.string.fetch_commits))
            }
        }
    }
}

@Composable
fun GitHubResponse(commits: MainViewModelCompose.Commits) {
    when (commits) {
        is MainViewModelCompose.Commits.Error -> CommitList(emptyList())
        MainViewModelCompose.Commits.Loading -> CommitList(emptyList())
        is MainViewModelCompose.Commits.Result -> CommitList(commits.commits)
    }
}

@Composable
fun CommitList(commits: List<Commit> = practiceData()) {
    LazyColumn {
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

private fun practiceData() = listOf(
    Commit(
      commit = CommitDetails(
          message = "test message",
          author = Author(name = "Sierra")
      )
    ),
    Commit(
        commit = CommitDetails(
            message = "test message",
            author = Author(name = "Sierra")
        )
    ),
    Commit(
        commit = CommitDetails(
            message = "test message",
            author = Author(name = "Sierra")
        )
    )
)