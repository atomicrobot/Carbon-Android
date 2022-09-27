package com.atomicrobot.carbon.data.api.github

import com.atomicrobot.carbon.app.provideGitHubApiService
import com.atomicrobot.carbon.app.provideRetrofit
import com.atomicrobot.carbon.data.api.github.model.Commit
import com.atomicrobot.carbon.loadResourceAsString
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.reactivex.observers.TestObserver
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.stopKoin
import org.mockito.MockitoAnnotations
import retrofit2.Converter
import retrofit2.Response
import retrofit2.converter.moshi.MoshiConverterFactory
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

class GitHubApiServiceTest {

    private lateinit var server: MockWebServer

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        server = MockWebServer()
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        server.shutdown()
        stopKoin()
    }

    @Test
    @Throws(Exception::class)
    fun testListCommitsSuccessful() {
        server.enqueue(MockResponse().setBody("/api/listCommits_success.json".loadResourceAsString()))
        server.start()

        val api = buildApi(server)
        val subscriber = TestObserver<Response<List<Commit>>>()
        api.listCommits("test_user", "test_repository").subscribe(subscriber)
        subscriber.await(1, TimeUnit.SECONDS)

        val serverRequest = server.takeRequest()
        assertEquals("GET", serverRequest.method)
        assertEquals("/repos/test_user/test_repository/commits", serverRequest.path)

        subscriber.assertNoErrors()
        subscriber.assertComplete()
        subscriber.assertValueCount(1)
        val response = subscriber.values()[0]
        assertTrue(response.isSuccessful)
        val commits = response.body()
        assertEquals(1, commits!!.size.toLong())
        val commit = commits[0]
        assertEquals("test message", commit.commitMessage)
        assertEquals("test author", commit.author)
    }

    @Test
    @Throws(Exception::class)
    fun testListCommitsUnsuccessful() {
        server.enqueue(MockResponse().setResponseCode(404).setBody("{\"message\": \"Not Found\"}"))
        server.start()

        val api = buildApi(server)
        val subscriber = TestObserver<Response<List<Commit>>>()
        api.listCommits("test_user", "test_repository").subscribe(subscriber)
        subscriber.await(1, TimeUnit.SECONDS)

        subscriber.assertNoErrors()
        subscriber.assertComplete()
        subscriber.assertValueCount(1)
        val response = subscriber.values()[0]
        assertFalse(response.isSuccessful)
        assertEquals(404, response.code().toLong())
    }

    @Test
    @Throws(Exception::class)
    fun testListCommitsNetworkError() {
        val api = buildApi("http://bad_url/")
        val subscriber = TestObserver<Response<List<Commit>>>()
        api.listCommits("test_user", "test_repository").subscribe(subscriber)
        subscriber.await(1, TimeUnit.SECONDS)

        subscriber.assertNoValues()
        assertEquals(1, subscriber.errors().size.toLong())
        val error = subscriber.errors()[0]
        assertTrue(error is UnknownHostException)
        // Note: You can't compare message text because that will be provided by the underlying runtime
    }

    @Throws(Exception::class)
    private fun buildApi(server: MockWebServer): GitHubApiService {
        val baseUrl = server.url("")
        return buildApi(baseUrl.toString())
    }

    @Throws(Exception::class)
    private fun buildApi(baseUrl: String): GitHubApiService {
        val client = OkHttpClient.Builder().build()
        // TODO - fix with koin tests?
        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        val converterFactory = MoshiConverterFactory.create(moshi) as Converter.Factory
        val retrofit = provideRetrofit(client, baseUrl, converterFactory)
        return provideGitHubApiService(retrofit)
    }
}
