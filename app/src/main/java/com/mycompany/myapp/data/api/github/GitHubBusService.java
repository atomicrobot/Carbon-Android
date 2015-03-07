package com.mycompany.myapp.data.api.github;

import com.mycompany.myapp.data.api.github.model.Commit;
import com.squareup.otto.Bus;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber.Tree;

public class GitHubBusService {
    private final GitHubApiService api;
    private final Bus bus;
    private final Tree logger;

    public GitHubBusService(GitHubApiService api, Bus bus, Tree logger) {
        this.api = api;
        this.bus = bus;
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

    public void loadCommits(final LoadCommitsRequest request) {
        api.listCommits(request.user, request.repository, new Callback<List<Commit>>() {
            @Override
            public void success(List<Commit> commits, Response response) {
                bus.post(new LoadCommitsResponse(request, commits));
            }

            @Override
            public void failure(RetrofitError error) {
                logger.e("Error loading commits", error);
            }
        });
    }
}
