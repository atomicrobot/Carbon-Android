package com.atomicrobot.carbon.ui.clickableCards

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.data.api.github.GitHubInteractor
import com.atomicrobot.carbon.data.api.github.model.DetailedCommit
import com.atomicrobot.carbon.ui.main.MainViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class GitCardInfoViewModel (
    private val app: Application,
    private val gitHubInteractor: GitHubInteractor,
    savedStateHandle: SavedStateHandle,
    private val loadingDelayMs: Long,
    ) : ViewModel() {
    var sha = mutableStateOf("")

    init {
        savedStateHandle.get<String>("sha")?.let {
            sha.value = it
        }
    }
    private val passSha = sha.value
    sealed class GitHubResponse {
        object Loading: GitHubResponse()
        class Result(val commit: DetailedCommit?) : GitHubResponse()
        class Error(val message: String) : GitHubResponse()
    }
    data class GitInfoScreenUiState(
        val username: String = MainViewModel.DEFAULT_USERNAME, // NON-NLS
        val repository: String = MainViewModel.DEFAULT_REPO, // NON-NLS
        val detailedCommitState: GitHubResponse = GitHubResponse.Result(null),
    )
    private val _uiState = MutableStateFlow(GitInfoScreenUiState())
    val uiState: StateFlow<GitInfoScreenUiState>
        get() = _uiState
    fun fetchDetailedCommit(sha:String) {
        // Update the UI state to indicate that we are loading.
        _uiState.value = _uiState.value.copy(
            detailedCommitState = GitHubResponse.Loading
        )
        viewModelScope.launch {
            try {
                /*Passes in active users credentials to interactor which will make use an API
                service to make a @Get request with said credentials*/
                gitHubInteractor.loadDetailedCommit(
                    GitHubInteractor.LoadDetailedCommitRequest(
                        uiState.value.username,
                        uiState.value.repository,
                        sha
                    )
                ).let {
                    _uiState.value = _uiState.value.copy(
                        detailedCommitState = GitHubResponse.Result(it.commit)
                    )
                }
            } catch (error: Exception) {
                Timber.e(error)
                _uiState.value = _uiState.value.copy(
                    detailedCommitState = GitHubResponse.Error(
                        error.message
                            ?: app.getString(R.string.error_unexpected)
                    )
                )
            }
        }
    }
}