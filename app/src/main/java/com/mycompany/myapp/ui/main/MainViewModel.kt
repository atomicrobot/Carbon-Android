package com.mycompany.myapp.ui.main

import android.app.Application
import androidx.databinding.Bindable
import android.os.Parcelable
import androidx.annotation.VisibleForTesting
import com.mycompany.myapp.BR
import com.mycompany.myapp.BuildConfig
import com.mycompany.myapp.R
import com.mycompany.myapp.data.api.github.GitHubInteractor
import com.mycompany.myapp.data.api.github.GitHubInteractor.LoadCommitsRequest
import com.mycompany.myapp.data.api.github.model.Commit
import com.mycompany.myapp.ui.BaseViewModel
import com.mycompany.myapp.ui.SimpleSnackbarMessage
import com.mycompany.myapp.util.RxUtils.delayAtLeast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.parcel.Parcelize
import javax.inject.Inject
import javax.inject.Named

class MainViewModel @Inject constructor(
        private val app: Application,
        private val gitHubInteractor: GitHubInteractor,
        @Named("loading_delay_ms") private val loadingDelayMs: Long)
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
        commits = Commits.Loading
        disposables.add(delayAtLeast(gitHubInteractor.loadCommits(LoadCommitsRequest(username, repository)), loadingDelayMs)
                .map { it.commits }  // Pull the commits out of the response
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { commits = Commits.Result(it) },
                        { commits = Commits.Error(it.message ?: app.getString(R.string.error_unexpected)) }))
    }

    companion object {
        private const val STATE_KEY = "MainViewModelState"  // NON-NLS
    }
}
