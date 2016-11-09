package com.mycompany.myapp.data.api.github;

import com.mycompany.myapp.data.api.github.model.Commit;

import java.util.List;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface GitHubApiService {
    @GET("/repos/{user}/{repository}/commits")
    Observable<Response<List<Commit>>> listCommits(
            @Path("user") String user,
            @Path("repository") String repository);
}
