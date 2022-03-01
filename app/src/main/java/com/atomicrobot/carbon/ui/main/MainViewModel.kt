package com.atomicrobot.carbon.ui.main

import android.app.Application
import android.os.Parcelable
import androidx.annotation.VisibleForTesting
import androidx.databinding.Bindable
import androidx.lifecycle.viewModelScope
import com.atomicrobot.carbon.BR
import com.atomicrobot.carbon.BuildConfig
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.data.api.github.GitHubInteractor
import com.atomicrobot.carbon.data.api.github.GitHubInteractor.LoadCommitsRequest
import com.atomicrobot.carbon.data.api.github.model.Commit
import com.atomicrobot.carbon.ui.BaseViewModel
import com.atomicrobot.carbon.ui.SimpleSnackbarMessage
import com.atomicrobot.carbon.util.CoroutineUtils.delayAtLeast
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import java.lang.Exception

class MainViewModel(
        private val app: Application,
        private val gitHubInteractor: GitHubInteractor,
        private val loadingDelayMs: Long)
    : BaseViewModel<MainViewModel.State>(app, STATE_KEY, State()) {

    @Parcelize
    class State(
            var username: String = "",
            var repository: String = "") : Parcelable

    sealed class Commits {
        object Loading : Commits()
        class Result(val commits: List<Commit>) : Commits()
        class Error(val message: String) : Commits()
    }

    override fun setupViewModel() {
        username = "madebyatomicrobot"  // NON-NLS
        repository = "android-starter-project"  // NON-NLS

        fetchCommits()
    }

    @VisibleForTesting
    internal var commits: Commits = Commits.Result(emptyList())
        set(value) {
            field = value

            notifyPropertyChanged(BR.loading)
            notifyPropertyChanged(BR.commits)
            notifyPropertyChanged(BR.fetchCommitsEnabled)

            when (value) {
                is Commits.Error -> snackbarMessage.value = value.message
                is Commits.Result -> {}
                Commits.Loading -> {}
            }
        }

    val snackbarMessage = SimpleSnackbarMessage()

    var username: String
        @Bindable get() = state.username
        set(value) {
            state.username = value
            notifyPropertyChanged(BR.username)
        }

    var repository: String
        @Bindable get() = state.repository
        set(value) {
            state.repository = value
            notifyPropertyChanged(BR.repository)
        }

    @Bindable("username", "repository")
    fun isFetchCommitsEnabled(): Boolean = commits !is Commits.Loading && !username.isEmpty() && !repository.isEmpty()

    @Bindable
    fun isLoading(): Boolean = commits is Commits.Loading

    @Bindable
    fun getCommits() = commits.let {
        when (it) {
            is Commits.Result -> it.commits
            else -> emptyList()
        }
    }

    fun getVersion(): String = BuildConfig.VERSION_NAME

    fun getFingerprint(): String = BuildConfig.VERSION_FINGERPRINT

    fun fetchCommits() {
        viewModelScope.launch {
            commits = Commits.Loading

            commits = delayAtLeast(loadingDelayMs) {
                try {
                    Commits.Result(
                        gitHubInteractor.loadCommits(
                            LoadCommitsRequest(username, repository)
                        ).commits
                    )
                } catch (e: Exception) {
                    Commits.Error(e.message ?: app.getString(R.string.error_unexpected))
                }
            }
        }
    }

    companion object {
        private const val STATE_KEY = "MainViewModelState"  // NON-NLS
    }
}
