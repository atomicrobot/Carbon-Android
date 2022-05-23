package com.atomicrobot.carbon.ui.main

import android.app.Application
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.data.api.github.GitHubInteractor
import com.atomicrobot.carbon.ui.BaseViewModel
import com.atomicrobot.carbon.util.RxUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModelCompose(
        private val app: Application,
        private val gitHubInteractor: GitHubInteractor,
        private val loadingDelayMs: Long)
    : BaseViewModel<MainViewModel.State>(app, STATE_KEY, MainViewModel.State()) {

    data class MainScreenUiState(
        val username: String = MainViewModel.DEFAULT_USERNAME,  // NON-NLS
        val repository: String = MainViewModel.DEFAULT_REPO, // NON-NLS
        val commitsState: MainViewModel.Commits = MainViewModel.Commits.Result(emptyList())
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
                repository = repository ?: _uiState.value.repository
        )
    }

    fun fetchCommits() {
        // Update the UI state to indicate that we are loading.
        _uiState.value = _uiState.value.copy(commitsState = MainViewModel.Commits.Loading)
        // Try and fetching the commit records
        disposables.add(
                RxUtils.delayAtLeast(
                        gitHubInteractor.loadCommits(
                                GitHubInteractor.LoadCommitsRequest(
                                        uiState.value.username,
                                        uiState.value.repository)),
                        loadingDelayMs)
                .map { it.commits }  // Pull the commits out of the response
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        // Update the UI state to display the commit results.
                        { _uiState.value = _uiState.value.copy(
                                commitsState = MainViewModel.Commits.Result(it))},
                        // Update the UI state to indicate we failed to load the commit results.
                        { _uiState.value = _uiState.value.copy(
                                commitsState = MainViewModel.Commits.Error(it.message
                                        ?: app.getString(R.string.error_unexpected)))}))
    }

    companion object {
        private const val STATE_KEY = "MainViewModelState"  // NON-NLS
    }
}