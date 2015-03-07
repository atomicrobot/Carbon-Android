package com.mycompany.myapp.data.api.github;

import com.mycompany.myapp.data.api.github.model.Commit;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

public interface GitHubApiService {
    @GET("/repos/{user}/{repository}/commits")
    void listCommits(@Path("user") String user, @Path("repository") String repository, Callback<List<Commit>> commits);
}
