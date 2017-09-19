package com.mycompany.myapp.data.api.github

import android.content.Context
import com.mycompany.myapp.Mockable
import com.mycompany.myapp.R
import com.mycompany.myapp.data.api.github.model.Commit

import io.reactivex.Observable
import retrofit2.Response
import timber.log.Timber

@Mockable
class GitHubInteractor(
        private val context: Context,
        private val api: GitHubApiService) {

    class LoadCommitsRequest(val user: String, val repository: String)
    class LoadCommitsResponse(val request: LoadCommitsRequest, val commits: List<Commit>)

    fun loadCommits(request: LoadCommitsRequest): Observable<LoadCommitsResponse> {
        return api.listCommits(request.user, request.repository)
                .toObservable()
                .map { response -> checkResponse(response, context.getString(R.string.error_get_commits_error)) }
                .map { response -> response.body() ?: emptyList() }
                .map { commits -> LoadCommitsResponse(request, commits) }
                .doOnError { error -> Timber.e(error) }
    }

    private fun <T> checkResponse(response: Response<T>, message: String): Response<T> {
        return when {
            response.isSuccessful -> response
            else -> throw IllegalStateException(message)
        }
    }
}
