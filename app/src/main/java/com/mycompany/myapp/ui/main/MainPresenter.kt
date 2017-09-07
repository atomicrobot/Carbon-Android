package com.mycompany.myapp.ui.main

import android.content.Context
import android.databinding.Bindable
import android.os.Parcelable
import com.mycompany.myapp.BR
import com.mycompany.myapp.BuildConfig
import com.mycompany.myapp.R
import com.mycompany.myapp.data.api.github.GitHubService
import com.mycompany.myapp.data.api.github.GitHubService.LoadCommitsRequest
import com.mycompany.myapp.data.api.github.model.Commit
import com.mycompany.myapp.databinding.ReadWriteBinding
import com.mycompany.myapp.ui.BasePresenter
import com.mycompany.myapp.ui.main.MainPresenter.MainViewContract
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import kotlinx.android.parcel.Parcelize
import java.util.concurrent.TimeUnit

class MainPresenter(
        private val context: Context,
        private val gitHubService: GitHubService,
        private val ioScheduler: Scheduler,
        private val mainScheduler: Scheduler,
        private val loadingDelayMs: Long)
    : BasePresenter<MainViewContract, MainPresenter.State>(STATE_KEY, State()) {

    interface MainViewContract {
        fun displayError(message: String)
    }

    @Parcelize
    data class CommitView(
            val message: String = "",
            val author: String = "") : Parcelable

    @Parcelize
    data class State(
            var initialized: Boolean = false,
            var username: String? = null,
            var repository: String? = null,
            var loadingCommits: Boolean = false,
            var commits: List<CommitView>? = null) : Parcelable

    override fun onResume() {
        super.onResume()
        if (!state.initialized) {
            state.initialized = true
            username = "madebyatomicrobot"  // NON-NLS
            repository = "android-starter-project"  // NON-NLS
            commits = ArrayList()
        }

        fetchCommits()
    }

    @get:Bindable var username: String? by ReadWriteBinding(BR.username) { state::username }
    @get:Bindable var repository: String? by ReadWriteBinding(BR.repository) { state::repository }
    @get:Bindable var loadingCommits: Boolean by ReadWriteBinding(BR.loadingCommits) { state::loadingCommits }
    @get:Bindable var commits: List<CommitView>? by ReadWriteBinding(BR.commits) { state::commits }

    @Bindable("username", "repository") fun isFetchCommitsEnabled(): Boolean {
        return !state.username.isNullOrEmpty() && !state.repository.isNullOrEmpty()
    }

    fun getVersion(): String = context.getString(R.string.version_format, BuildConfig.VERSION_NAME)

    fun getFingerprint(): String = context.getString(R.string.fingerprint_format, BuildConfig.VERSION_FINGERPRINT)

    fun fetchCommits() {
        disposables.add(loadCommits(LoadCommitsRequest(state.username, state.repository)))
    }

    private fun loadCommits(request: LoadCommitsRequest): Disposable {
        return delayAtLeast(gitHubService.loadCommits(request), loadingDelayMs)
                .flatMap<Commit> { response -> Observable.fromIterable<Commit>(response.commits) }
                .map(this::toCommitView)
                .toList()
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
                .subscribe({ commits = it }, { handleError(it) })
    }

    private fun <T> delayAtLeast(observable: Observable<T>, delayMs: Long): Observable<T> {
        val timer = Observable.timer(delayMs, TimeUnit.MILLISECONDS)
        return Observable.combineLatest<Long, T, T>(timer, observable, BiFunction { _, response -> response })
    }

    private fun toCommitView(commit: Commit): CommitView {
        return CommitView(
                message = commit.commitMessage,
                author = context.getString(R.string.author_format, commit.author))
    }

    private fun handleError(throwable: Throwable) {
        val message = throwable.message!!
        view.displayError(message)
    }

    companion object {
        private val STATE_KEY = "MainPresenterState"  // NON-NLS
    }
}
