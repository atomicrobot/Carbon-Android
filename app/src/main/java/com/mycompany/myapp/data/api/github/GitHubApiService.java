package com.mycompany.myapp.data.api.github;

import com.mycompany.myapp.data.api.github.model.Commit;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

public interface GitHubApiService {
    @GET("/repos/{user}/{repository}/commits")
    Observable<List<Commit>> listCommits(@Path("user") String user, @Path("repository") String repository);
}
