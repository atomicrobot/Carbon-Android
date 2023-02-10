package com.atomicrobot.carbon.ui.clickableCards

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.data.api.github.GitHubInteractor
import com.atomicrobot.carbon.data.api.github.model.DetailedCommit
import com.atomicrobot.carbon.ui.main.MainViewModel
import kotlinx.coroutines.launch
import timber.log.Timber

class GitCardInfoViewModel (
    private val app: Application,
    private val gitHubInteractor: GitHubInteractor,
    private val loadingDelayMs: Long,
    ) : ViewModel() {

    sealed class DetailedCommits {
        object Loading: DetailedCommits()
        class Result(val commit: List<DetailedCommit>) : DetailedCommits()
        class Error(val message: String) : DetailedCommits()
    }
    data class MainScreenUiState(
        val username: String = MainViewModel.DEFAULT_USERNAME, // NON-NLS
        val repository: String = MainViewModel.DEFAULT_REPO, // NON-NLS
        val detailedCommitState: DetailedCommits = DetailedCommits.Result(emptyList())
    )

//    fun fetchDetailedCommit() {
//        // Update the UI state to indicate that we are loading.
//        _uiState.value = _uiState.value.copy(detailedCommitState = MainViewModel.DetailedCommits.Loading)
//        viewModelScope.launch {
//            try {
//                /*Passes in active users credentials to interactor which will make use an API
//                service to make a @Get request with said credentials*/
//                gitHubInteractor.loadDetailedCommit(
//                    GitHubInteractor.LoadCommitsRequest(
//                        uiState.value.username,
//                        uiState.value.repository
//                    )
//                ).let {
//                    _uiState.value = _uiState.value.copy(detailedCommitState = MainViewModel.DetailedCommits.Result(it.commit))
//                }
//                //TODO
//                /* add function to githubinteractor to get a single detailed commit, and call here*/
//            } catch (error: Exception) {
//                Timber.e(error)
//                _uiState.value = _uiState.value.copy(
//                    detailedCommitState = MainViewModel.DetailedCommits.Error(
//                        error.message
//                            ?: app.getString(R.string.error_unexpected)
//                    )
//                )
//            }
//        }
//    }
}