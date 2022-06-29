package com.atomicrobot.carbon.ui.main

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.atomicrobot.carbon.BuildConfig
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.app.LoadingDelayMs
import com.atomicrobot.carbon.data.api.github.GitHubInteractor
import com.atomicrobot.carbon.data.api.github.model.Commit
import com.atomicrobot.carbon.ui.BaseViewModel
import com.atomicrobot.carbon.ui.deeplink.DeepLinkInteractor
import com.atomicrobot.carbon.util.CoroutineUtils.delayAtLeast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainViewModelCompose @Inject constructor(
    private val app: Application,
    private val gitHubInteractor: GitHubInteractor,
    @LoadingDelayMs private val loadingDelayMs: Long,
) : BaseViewModel(app){

    sealed class Commits {
        object Loading : Commits()
        class Result(val commits: List<Commit>) : Commits()
        class Error(val message: String) : Commits()
    }

    data class MainScreenUiState(
        val username: String = "madebyatomicrobot",  // NON-NLS
        val repository: String = "android-starter-project", // NON-NLS
        val commitsState: Commits = Commits.Result(emptyList())
    )

    private val _uiState = MutableStateFlow(MainScreenUiState())
    val uiState: StateFlow<MainScreenUiState>
        get() = _uiState

    override fun setupViewModel() {
        fetchCommits()
    }

    fun updateUserInput(username: String?, repository: String?) {
        _uiState.value = _uiState.value.copy(
            username = username ?: _uiState.value.username,
            repository = repository ?:  _uiState.value.repository,
        )
    }

    fun fetchCommits() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(commitsState = Commits.Loading)

            _uiState.value = _uiState.value.copy(
                commitsState = delayAtLeast(loadingDelayMs) {
                    try {
                        Commits.Result(
                            gitHubInteractor.loadCommits(
                                GitHubInteractor.LoadCommitsRequest(
                                    uiState.value.username,
                                    uiState.value.repository
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
    fun getVersion(): String = BuildConfig.VERSION_NAME

    fun getFingerprint(): String = BuildConfig.VERSION_FINGERPRINT


    companion object {
        private const val STATE_KEY = "MainViewModelState"  // NON-NLS
        const val DEFAULT_USERNAME = "madebyatomicrobot"  // NON-NLS
        const val DEFAULT_REPO = "android-starter-project"  // NON-NLS
    }
}