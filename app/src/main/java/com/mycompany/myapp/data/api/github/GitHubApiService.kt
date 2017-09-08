package com.mycompany.myapp.data.api.github

import com.mycompany.myapp.data.api.github.model.Commit

import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubApiService {
    @GET("repos/{user}/{repository}/commits")
    fun listCommits(
            @Path("user") user: String,
            @Path("repository") repository: String): Single<Response<List<Commit>>>
}
