package com.mycompany.myapp.ui.main

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.mycompany.myapp.BuildConfig
import com.mycompany.myapp.app.MainApplication
import com.mycompany.myapp.data.api.github.GitHubService
import com.mycompany.myapp.data.api.github.GitHubService.LoadCommitsRequest
import com.mycompany.myapp.data.api.github.GitHubService.LoadCommitsResponse
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

class SampleViewModel(application: Application) : AndroidViewModel(application) {

    private val subscriptions = CompositeSubscription()
    private val viewState = MutableLiveData<SampleFragmentState>()

    @Inject lateinit var gitHubService: GitHubService

    init {
        (application as MainApplication).component.inject(this)
        val initialState = SampleFragmentState(versionCode = BuildConfig.VERSION_NAME, versionFingerPrint = BuildConfig.VERSION_FINGERPRINT)
        viewState.value = initialState
    }

    fun getViewState(): LiveData<SampleFragmentState> {
        return viewState
    }

    fun fetchCommits() {
        subscriptions.add(gitHubService.loadCommits(buildLoadCommitsRequest())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe({ handleCommitResponse(it) }, { handleError(it) }))
    }

    private fun handleCommitResponse(loadCommitsResponse: LoadCommitsResponse) {
        val commits = loadCommitsResponse.commits
        val state = viewState.value
        viewState.postValue(state?.copy(commits = commits, errorMessage = null))
    }

    private fun handleError(throwable: Throwable) {
        val message = throwable.message
        val state = viewState.value
        viewState.postValue(state?.copy(commits = emptyList(), errorMessage = message))
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.unsubscribe()
    }

    private fun buildLoadCommitsRequest(): LoadCommitsRequest? {
        viewState.value?.let { state ->
            return LoadCommitsRequest(state.username, state.repository)
        }

        return null
    }

    fun setUsername(username: String) {
        val state = viewState.value
        viewState.value = state?.copy(username = username)
    }

    fun setRepository(repositoryString: String) {
        val state = viewState.value
        viewState.value = state?.copy(repository = repositoryString)
    }
}
