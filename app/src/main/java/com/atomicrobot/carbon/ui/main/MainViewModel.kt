package com.atomicrobot.carbon.ui.main

import android.app.Application
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atomicrobot.carbon.BuildConfig
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.app.LoadingDelayMs
import com.atomicrobot.carbon.data.api.github.GitHubInteractor
import com.atomicrobot.carbon.data.api.github.model.Commit
import com.atomicrobot.carbon.util.CoroutineUtils.delayAtLeast
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val app: Application,
    private val gitHubInteractor: GitHubInteractor,
    @LoadingDelayMs private val loadingDelayMs: Long,
) : ViewModel() {

    sealed class Commits {
        object Loading : Commits()
        class Result(val commits: List<Commit>) : Commits()
        class Error(val message: String) : Commits()
    }

    data class MainScreenViewState(
        var username: String = "madebyatomicrobot", // NON-NLS
        var repository: String = "android-starter-project", // NON-NLS
        var commitsState: Commits = Commits.Result(emptyList())
    )

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val _viewState = MutableStateFlow(MainScreenViewState())
    val viewState = _viewState.asStateFlow()

    fun updateUserInput(username: String?, repository: String?) {
        _viewState.value = _viewState.value.copy(
            username = username ?: _viewState.value.username,
            repository = repository ?: _viewState.value.repository,
        )
    }

    fun fetchCommits() {
        viewModelScope.launch {
            _viewState.value = _viewState.value.copy(commitsState = Commits.Loading)

            _viewState.value = _viewState.value.copy(
                commitsState = delayAtLeast(loadingDelayMs) {
                    try {
                        Commits.Result(
                            gitHubInteractor.loadCommits(
                                GitHubInteractor.LoadCommitsRequest(
                                    viewState.value.username,
                                    viewState.value.repository
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
        private const val STATE_KEY = "MainViewModelState" // NON-NLS
        const val DEFAULT_USERNAME = "madebyatomicrobot" // NON-NLS
        const val DEFAULT_REPO = "android-starter-project" // NON-NLS
    }
}