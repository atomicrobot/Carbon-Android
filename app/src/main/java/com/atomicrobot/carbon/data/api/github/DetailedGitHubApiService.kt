package com.atomicrobot.carbon.data.api.github

import com.atomicrobot.carbon.data.api.github.model.DetailedCommit
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DetailedGitHubApiService {
    @GET("repos/{user}/{repository}/commits")
    suspend fun detailedCommit(
        @Path("user") user: String,
        @Path("repository") repository: String
    ): Response<List<DetailedCommit>>
}