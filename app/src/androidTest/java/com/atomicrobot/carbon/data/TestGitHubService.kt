package com.atomicrobot.carbon.data

import com.atomicrobot.carbon.data.api.github.GitHubApiService
import com.atomicrobot.carbon.data.api.github.model.Author
import com.atomicrobot.carbon.data.api.github.model.Commit
import com.atomicrobot.carbon.data.api.github.model.CommitDetails
import retrofit2.Response

class TestGitHubService : GitHubApiService {
    override suspend fun listCommits(user: String, repository: String): Response<List<Commit>> {
        val commit = Commit(CommitDetails("Test commit message", Author("Test author")))
        return Response.success(listOf(commit))
    }
}