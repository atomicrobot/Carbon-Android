package com.mycompany.myapp.data.api.github;

import com.mycompany.myapp.data.api.github.GitHubService.LoadCommitsRequest;
import com.mycompany.myapp.data.api.github.GitHubService.LoadCommitsResponse;
import com.mycompany.myapp.data.api.github.model.Commit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import retrofit2.Response;

import static com.mycompany.myapp.data.api.github.model.CommitTestHelper.stubCommit;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class GitHubServiceTest {

    @Mock GitHubApiService api;

    private GitHubService service;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        service = new GitHubService(api);
    }

    @Test
    public void testLoadCommits() throws Exception {
        Single<Response<List<Commit>>> mockResponse = Single.just(Response.success(asList(stubCommit("test name", "test message"))));
        when(api.listCommits(eq("user"), eq("repo"))).thenReturn(mockResponse);

        TestObserver<LoadCommitsResponse> subscriber = new TestObserver<>();
        service.loadCommits(new LoadCommitsRequest("user", "repo")).subscribeWith(subscriber);
        subscriber.await(1, TimeUnit.SECONDS);

        subscriber.assertValueCount(1);
        subscriber.assertNoErrors();
        subscriber.assertComplete();

        LoadCommitsResponse response = subscriber.values().get(0);
        assertEquals("user", response.getRequest().getUser());
        assertEquals("repo", response.getRequest().getRepository());
        assertEquals(1, response.getCommits().size());

        Commit commit = response.getCommits().get(0);
        assertEquals("test name", commit.getAuthor());
        assertEquals("test message", commit.getCommitMessage());
    }
}