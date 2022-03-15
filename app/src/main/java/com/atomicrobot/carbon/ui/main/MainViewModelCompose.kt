package com.atomicrobot.carbon.ui.main

import android.app.Application
import android.os.Parcelable
import androidx.lifecycle.viewModelScope
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.app.LoadingDelayMs
import com.atomicrobot.carbon.data.api.github.GitHubInteractor
import com.atomicrobot.carbon.data.api.github.model.Commit
import com.atomicrobot.carbon.ui.BaseViewModel
import com.atomicrobot.carbon.util.CoroutineUtils
import com.atomicrobot.carbon.util.CoroutineUtils.delayAtLeast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainViewModelCompose @Inject constructor(
    val app: Application,
    private val gitHubInteractor: GitHubInteractor,
    @LoadingDelayMs private val loadingDelayMs: Long
) : BaseViewModel<MainViewModelCompose.State>(app, STATE_KEY, State()) {

    @Parcelize
    class State(
        var username: String = "",
        var repository: String = ""
    ) : Parcelable

    sealed class Commits {
        object Loading : Commits()
        class Result(val commits: List<Commit>) : Commits()
        class Error(val message: String) : Commits()
    }

    data class MainScreenUiState(
        val state: State = State(
            username = "madebyatomicrobot",
            repository = "android-starter-project"
        ),
        val commits: Commits = Commits.Result(emptyList())
    )

    private val _uiState = MutableStateFlow(MainScreenUiState())
    val uiState: StateFlow<MainScreenUiState>
        get() = _uiState

    override fun setupViewModel() {
        fetchCommits()
    }

    fun updateUserInput(username: String?, repository: String?) {
        _uiState.value = _uiState.value.copy(
            state = State(
                username = username ?: "",
                repository = repository ?: ""
            )
        )
    }

    fun fetchCommits() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(commits = Commits.Loading)

            _uiState.value = _uiState.value.copy(
                commits = delayAtLeast(loadingDelayMs) {
                    try {
                        Commits.Result(
                            gitHubInteractor.loadCommits(
                                GitHubInteractor.LoadCommitsRequest(
                                    uiState.value.state.username,
                                    uiState.value.state.repository
                                )
                            ).commits
                        )
                    } catch (e: Exception) {
                        Commits.Error(
                            e.message ?: app.getString(R.string.error_unexpected)
                        )
                    }
                }
            )
        }
    }

    companion object {
        private const val STATE_KEY = "MainViewModelState"  // NON-NLS
    }
}