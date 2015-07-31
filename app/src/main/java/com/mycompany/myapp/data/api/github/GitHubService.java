package com.mycompany.myapp.data.api.github;

import com.mycompany.myapp.data.api.github.model.Commit;

import java.util.List;

import rx.Observable;
import timber.log.Timber.Tree;

public class GitHubService {
    private final GitHubApiService api;
    private final Tree logger;

    public GitHubService(GitHubApiService api, Tree logger) {
        this.api = api;
        this.logger = logger;
    }

    public static class LoadCommitsRequest {
        private final String user;
        private final String repository;

        public LoadCommitsRequest(String user, String repository) {
            this.user = user;
            this.repository = repository;
        }

        public String getUser() {
            return user;
        }

        public String getRepository() {
            return repository;
        }
    }

    public static class LoadCommitsResponse {
        private final LoadCommitsRequest request;
        private final List<Commit> commits;

        public LoadCommitsResponse(LoadCommitsRequest request, List<Commit> commits) {
            this.request = request;
            this.commits = commits;
        }

        public LoadCommitsRequest getRequest() {
            return request;
        }

        public List<Commit> getCommits() {
            return commits;
        }
    }

    public Observable<LoadCommitsResponse> loadCommits(final LoadCommitsRequest request) {
        return api.listCommits(request.user, request.repository)
                .map(commits -> new LoadCommitsResponse(request, commits));
    }
}
