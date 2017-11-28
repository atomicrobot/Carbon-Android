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

    data class CommitView(
            val message: String = "",
            val author: String = "")

    @Parcelize
    data class State(
            var username: String = "",
            var repository: String = "") : Parcelable

    fun onResume() {
        if (!viewModelInitialized) {
            viewModelInitialized = true
            username = "madebyatomicrobot"  // NON-NLS
            repository = "android-starter-project"  // NON-NLS

            fetchCommits()
        }
    }

    var snackbarMessage = SimpleSnackbarMessage()

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

    var loadingCommits: Boolean = false
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.loadingCommits)
        }

    var commits: List<CommitView> = emptyList()
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.commits)
        }

    @Bindable("username", "repository")
    fun isFetchCommitsEnabled(): Boolean = !username.isEmpty() && !repository.isEmpty()

    fun getVersion(): String = app.getString(R.string.version_format, BuildConfig.VERSION_NAME)

    fun getFingerprint(): String = app.getString(R.string.fingerprint_format, BuildConfig.VERSION_FINGERPRINT)

    fun fetchCommits() {
        loadingCommits = true
        disposables.add(
                delayAtLeast(loadCommits(state.username, state.repository), loadingDelayMs)
                        .map { commits -> commits.map { toCommitView(it) } }
                        .doFinally({ loadingCommits = false })
                        .subscribeOn(ioScheduler)
                        .observeOn(mainScheduler)
                        .subscribe({ commits = it }, { handleError(it) }))
    }

    private fun loadCommits(username: String, repository: String): Observable<List<Commit>> {
        val request = LoadCommitsRequest(username, repository)
        return gitHubInteractor.loadCommits(request)
                .map { it.commits }
    }

    private fun toCommitView(commit: Commit): CommitView {
        return CommitView(
                message = commit.commitMessage,
                author = app.getString(R.string.author_format, commit.author))
    }

    private fun handleError(throwable: Throwable) {
        commits = emptyList()
        snackbarMessage.value = throwable.message ?: app.getString(R.string.error_unexpected)
    }

    companion object {
        private const val STATE_KEY = "MainViewModelState"  // NON-NLS
    }
}
