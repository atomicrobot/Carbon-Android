package com.atomicrobot.carbon.data.api.github

import com.atomicrobot.carbon.data.api.github.model.DetailedCommit
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DetailedGitHubApiService {
    @GET("repos/{user}/{repository}/commits/{sha}")
    suspend fun detailedCommit(
        @Path("user") user: String,
        @Path("repository") repository: String,
        @Path("sha") sha: String
    ): Response<DetailedCommit>
}
