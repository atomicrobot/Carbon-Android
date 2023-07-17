package com.atomicrobot.carbon.ui.main

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atomicrobot.carbon.BuildConfig
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.data.api.github.GitHubInteractor
import com.atomicrobot.carbon.data.api.github.model.Commit
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel(
    private val app: Application,
    private val gitHubInteractor: GitHubInteractor,
    private val loadingDelayMs: Long,
) : ViewModel() {

    sealed class Commits {
        object Loading : Commits()
        class Result(val commits: List<Commit>) : Commits()
        class Error(val message: String) : Commits()
    }
    sealed class CardClicked {
        data class PassSha(val sha: String) : CardClicked()
        object Clicked : CardClicked()
    }
    sealed class ClickAction {
        object Success : ClickAction()
    }

    data class MainScreenUiState(
        val username: String = DEFAULT_USERNAME, // NON-NLS
        val repository: String = DEFAULT_REPO, // NON-NLS
        val commitsState: Commits = Commits.Result(emptyList()),
        val sha: String = ""
    )

    private val _uiState = MutableStateFlow(MainScreenUiState())
    val uiState: StateFlow<MainScreenUiState>
        get() = _uiState

    fun updateUserInput(username: String?, repository: String?) {
        _uiState.value = _uiState.value.copy(
            username = username ?: _uiState.value.username,
            repository = repository ?: _uiState.value.repository
        )
    }

    fun fetchCommits() {
        // Update the UI state to indicate that we are loading.
        _uiState.value = _uiState.value.copy(commitsState = Commits.Loading)
        // Try and fetching the commit records
        viewModelScope.launch {
            try {
                gitHubInteractor.loadCommits(
                    GitHubInteractor.LoadCommitsRequest(
                        uiState.value.username,
                        uiState.value.repository
                    )
                ).let {
                    _uiState.value = _uiState.value.copy(commitsState = Commits.Result(it.commits))
                }
            } catch (error: Exception) {
                Timber.e(error)
                _uiState.value = _uiState.value.copy(
                    commitsState = Commits.Error(
                        error.message
                            ?: app.getString(R.string.error_unexpected)
                    )
                )
            }
        }
    }

    fun getVersion() = BuildConfig.VERSION_NAME

    fun getVersionFingerprint() = BuildConfig.VERSION_FINGERPRINT

    fun onAction(action: CardClicked) {
        when (action) {
            is CardClicked.Clicked -> {
                viewModelScope.launch {
                    clickAction.emit(ClickAction.Success)
                }
            }
            is CardClicked.PassSha -> {
                viewModelScope.launch {
                    _uiState.value = _uiState.value.copy(
                        sha = action.sha
                    )
                    clickAction.emit(ClickAction.Success)
                }
            }
        }
    }
    val clickAction = MutableSharedFlow<ClickAction>()

    companion object {
        const val DEFAULT_USERNAME = "madebyatomicrobot" // NON-NLS
        const val DEFAULT_REPO = "android-starter-project" // NON-NLS
    }
}
