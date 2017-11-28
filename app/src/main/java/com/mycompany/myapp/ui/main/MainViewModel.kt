package com.mycompany.myapp.ui.main

import android.app.Application
import android.databinding.Bindable
import android.os.Parcelable
import com.mycompany.myapp.BR
import com.mycompany.myapp.BuildConfig
import com.mycompany.myapp.R
import com.mycompany.myapp.data.api.github.GitHubInteractor
import com.mycompany.myapp.data.api.github.GitHubInteractor.LoadCommitsRequest
import com.mycompany.myapp.data.api.github.model.Commit
import com.mycompany.myapp.ui.BaseViewModel
import com.mycompany.myapp.ui.SimpleSnackbarMessage
import com.mycompany.myapp.util.RxUtils.delayAtLeast
import io.reactivex.Observable
import io.reactivex.Scheduler
import kotlinx.android.parcel.Parcelize
import javax.inject.Inject
import javax.inject.Named

class MainViewModel @Inject constructor(
        private val app: Application,
        private val gitHubInteractor: GitHubInteractor,
        @Named("io") private val ioScheduler: Scheduler,
        @Named("main") private val mainScheduler: Scheduler,
        @Named("loading_delay_ms") private val loadingDelayMs: Long)
    : BaseViewModel<MainViewModel.State>(app, STATE_KEY, State()) {

    private var viewModelInitialized: Boolean = false

    var snackbarMessage = SimpleSnackbarMessage()

    @Parcelize
    data class State(
            var username: String = "",
            var repository: String = "") : Parcelable

    data class CommitView(
            val message: String = "",
            val author: String = "")

    sealed class Results {
        class Loading : Results()
        class Commits(val commits: List<CommitView>) : Results()
        class Error(val message: String) : Results()
    }

    private var results: Results = Results.Loading()
        set(value) {
            field = value

            notifyPropertyChanged(BR.loadingCommits)
            notifyPropertyChanged(BR.commits)
            notifyPropertyChanged(BR.fetchCommitsEnabled)

            when (value) {
                is Results.Error -> snackbarMessage.value = value.message
            }
        }

    fun onResume() {
        if (!viewModelInitialized) {
            viewModelInitialized = true
            username = "madebyatomicrobot"  // NON-NLS
            repository = "android-starter-project"  // NON-NLS

            fetchCommits()
        }
    }

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
    fun isFetchCommitsEnabled(): Boolean = results !is Results.Loading && !username.isEmpty() && !repository.isEmpty()

    @Bindable fun isLoadingCommits(): Boolean = results is Results.Loading

    @Bindable fun getCommits() = results.let {
        when (it) {
            is Results.Commits -> it.commits
            else -> emptyList()
        }
    }

    fun getVersion(): String = app.getString(R.string.version_format, BuildConfig.VERSION_NAME)

    fun getFingerprint(): String = app.getString(R.string.fingerprint_format, BuildConfig.VERSION_FINGERPRINT)

    fun fetchCommits() {
        results = Results.Loading()
        disposables.add(
                delayAtLeast(loadCommits(state.username, state.repository), loadingDelayMs)
                        .map { commits -> commits.map { toCommitView(it) } }
                        .subscribeOn(ioScheduler)
                        .observeOn(mainScheduler)
                        .subscribe(
                                { commits -> results = Results.Commits(commits) },
                                { error -> results = Results.Error(error.message ?: app.getString(R.string.error_unexpected)) }))
    }

    private fun loadCommits(username: String, repository: String): Observable<List<Commit>> {
        return gitHubInteractor.loadCommits(LoadCommitsRequest(username, repository))
                .map { it.commits }
    }

    private fun toCommitView(commit: Commit): CommitView {
        return CommitView(
                message = commit.commitMessage,
                author = app.getString(R.string.author_format, commit.author))
    }

    companion object {
        private const val STATE_KEY = "MainViewModelState"  // NON-NLS
    }
}
