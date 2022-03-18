package com.atomicrobot.carbon.ui.main

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.app.LoadingDelayMs
import com.atomicrobot.carbon.data.api.github.GitHubInteractor
import com.atomicrobot.carbon.data.api.github.model.Commit
import com.atomicrobot.carbon.ui.BaseViewModel
import com.atomicrobot.carbon.util.CoroutineUtils.delayAtLeast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainViewModelCompose @Inject constructor(
    val app: Application,
    private val gitHubInteractor: GitHubInteractor,
    @LoadingDelayMs private val loadingDelayMs: Long
) : BaseViewModel<MainViewModel.State>(app, STATE_KEY, MainViewModel.State()) {
    // ^ need to change this. research what should be in a BaseVM for compose app
    // Can remove all the databinding observable functions  

    sealed class CommitsState {
        object Loading : CommitsState()
        class Result(val commits: List<Commit>) : CommitsState()
        class Error(val message: String) : CommitsState()
    }

    data class MainScreenUiState(
        val username: String = "madebyatomicrobot",  // NON-NLS
        val repository: String = "android-starter-project", // NON-NLS
        val commitsState: CommitsState = CommitsState.Result(emptyList())
    )

    private val _uiState = MutableStateFlow(MainScreenUiState())
    val uiState: StateFlow<MainScreenUiState>
        get() = _uiState

    override fun setupViewModel() {
        fetchCommits()
    }

    fun updateUserInput(username: String?, repository: String?) {
        _uiState.value = _uiState.value.copy(
            username = username ?: "",
            repository = repository ?: ""
        )
    }

    fun fetchCommits() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(commitsState = CommitsState.Loading)

            _uiState.value = _uiState.value.copy(
                commitsState = delayAtLeast(loadingDelayMs) {
                    try {
                        CommitsState.Error("Sierra")
//                        CommitsState.Result(
//                            gitHubInteractor.loadCommits(
//                                GitHubInteractor.LoadCommitsRequest(
//                                    uiState.value.username,
//                                    uiState.value.repository
//                                )
//                            ).commits
//                        )
                    } catch (e: Exception) {
                        CommitsState.Error(
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