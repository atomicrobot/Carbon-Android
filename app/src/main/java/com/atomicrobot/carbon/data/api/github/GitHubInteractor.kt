package com.atomicrobot.carbon.data.api.github

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.atomicrobot.carbon.Mockable
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.data.api.github.model.Commit
import com.atomicrobot.carbon.data.api.github.model.DetailedCommit
import retrofit2.Response
import timber.log.Timber

@Mockable
class GitHubInteractor(
    private val context: Context,
    private val api: GitHubApiService,
    private val api2: DetailedGitHubApiService
) {

    class LoadCommitsRequest(val user: String, val repository: String)
    class LoadCommitsResponse(val request: LoadCommitsRequest, val commits: List<Commit>)

    class LoadDetailedCommitRequest(val user: String, val repository: String, val sha: String)
    class LoadDetailedCommitResponse(val request: LoadDetailedCommitRequest, val commit: DetailedCommit)

    suspend fun loadCommits(request: LoadCommitsRequest): LoadCommitsResponse {
        val response = checkResponse(
            api.listCommits(request.user, request.repository),
            context.getString(R.string.error_get_commits_error)
        )
        val commits: List<Commit> = response.body() ?: emptyList()
        return LoadCommitsResponse(request, commits)
    }

    suspend fun loadDetailedCommit(request: LoadDetailedCommitRequest): LoadDetailedCommitResponse {
        val detailedResponse = checkResponse(
            api2.detailedCommit(request.user, request.repository, request.sha),
            context.getString(R.string.error_get_detailed_commit_error)
        )
        val detailedCommit: DetailedCommit = detailedResponse.body() ?: emptyList<DetailedCommit>()[0]
        return LoadDetailedCommitResponse(request, detailedCommit)
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
