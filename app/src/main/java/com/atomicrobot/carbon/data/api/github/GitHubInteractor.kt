package com.atomicrobot.carbon.data.api.github

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.atomicrobot.carbon.Mockable
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.data.api.github.model.Commit
import retrofit2.Response
import timber.log.Timber

@Mockable
class GitHubInteractor(
    private val context: Context,
    private val api: GitHubApiService
) {

    class LoadCommitsRequest(val user: String, val repository: String)
    class LoadCommitsResponse(val request: LoadCommitsRequest, val commits: List<Commit>)

    suspend fun loadCommits(request: LoadCommitsRequest): LoadCommitsResponse {
        return LoadCommitsResponse(
            request = request,
            commits = checkResponse(
                api.listCommits(request.user, request.repository),
                context.getString(R.string.error_get_commits_error)
            ).body() ?: emptyList()
        )
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun <T> checkResponse(response: Response<T>, message: String): Response<T> {
        return when {
            response.isSuccessful -> response
            else -> {
                Timber.e(response.errorBody()?.string() ?: message)
                throw IllegalStateException(message)
            }
        }
    }
}
