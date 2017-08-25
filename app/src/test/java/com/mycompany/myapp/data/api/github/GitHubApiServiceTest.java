package com.mycompany.myapp.data.api.github;

import com.mycompany.myapp.data.DataModule;
import com.mycompany.myapp.data.api.github.model.Commit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.observers.TestObserver;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.mycompany.myapp.TestUtil.loadResourceAsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GitHubApiServiceTest {

    private MockWebServer server;

    @Before
    public void setup() {
        server = new MockWebServer();
    }

    @After
    public void tearDown() throws Exception {
        server.shutdown();
    }

    @Test
    public void testListCommitsSuccessful() throws Exception {
        server.enqueue(new MockResponse().setBody(loadResourceAsString("/api/listCommits_success.json")));
        server.start();

        GitHubApiService api = buildApi(server);
        TestObserver<Response<List<Commit>>> subscriber = new TestObserver<>();
        api.listCommits("test_user", "test_repository").subscribe(subscriber);
        subscriber.await(1, TimeUnit.SECONDS);

        RecordedRequest serverRequest = server.takeRequest();
        assertEquals("GET", serverRequest.getMethod());
        assertEquals("/repos/test_user/test_repository/commits", serverRequest.getPath());

        subscriber.assertNoErrors();
        subscriber.assertComplete();
        subscriber.assertValueCount(1);
        Response<List<Commit>> response = subscriber.values().get(0);
        assertTrue(response.isSuccessful());
        List<Commit> commits = response.body();
        assertEquals(1, commits.size());
        Commit commit = commits.get(0);
        assertEquals("test message", commit.getCommitMessage());
        assertEquals("test author", commit.getAuthor());
    }

    @Test
    public void testListCommitsUnsuccessful() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(404).setBody("{\"message\": \"Not Found\"}"));
        server.start();

        GitHubApiService api = buildApi(server);
        TestObserver<Response<List<Commit>>> subscriber = new TestObserver<>();
        api.listCommits("test_user", "test_repository").subscribe(subscriber);
        subscriber.await(1, TimeUnit.SECONDS);

        subscriber.assertNoErrors();
        subscriber.assertComplete();
        subscriber.assertValueCount(1);
        Response<List<Commit>> response = subscriber.values().get(0);
        assertFalse(response.isSuccessful());
        assertEquals(404, response.code());
    }

    @Test
    public void testListCommitsNetworkError() throws Exception {
        GitHubApiService api = buildApi("http://bad_url/");
        TestObserver<Response<List<Commit>>> subscriber = new TestObserver<>();
        api.listCommits("test_user", "test_repository").subscribe(subscriber);
        subscriber.await(1, TimeUnit.SECONDS);

        subscriber.assertNoValues();
        assertEquals(1, subscriber.errors().size());
        Throwable error = subscriber.errors().get(0);
        assertTrue(error instanceof UnknownHostException);
        // Note: You can't compare message text because that will be provided by the underlying runtime
    }

    private GitHubApiService buildApi(MockWebServer server) throws Exception {
        HttpUrl baseUrl = server.url("");
        return buildApi(baseUrl.toString());
    }

    private GitHubApiService buildApi(String baseUrl) throws Exception {
        DataModule module = new DataModule();
        OkHttpClient client = module.provideOkHttpClient(null);
        Converter.Factory converterFactory = module.provideConverter();
        Retrofit retrofit = module.provideRetrofit(client, baseUrl, converterFactory);
        return module.provideGitHubApiService(retrofit);
    }
}