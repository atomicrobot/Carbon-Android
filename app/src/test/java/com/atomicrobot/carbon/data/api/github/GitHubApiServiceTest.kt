package com.atomicrobot.carbon.data.api.github

import com.atomicrobot.carbon.app.provideGitHubApiService
import com.atomicrobot.carbon.app.provideRetrofit
import com.atomicrobot.carbon.loadResourceAsString
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.koin.core.context.stopKoin
import org.mockito.MockitoAnnotations
import retrofit2.Converter
import retrofit2.converter.moshi.MoshiConverterFactory
import java.net.UnknownHostException

class GitHubApiServiceTest {

    private lateinit var server: MockWebServer

    @Before
    fun setup() {
        stopKoin()
        MockitoAnnotations.openMocks(this)
        server = MockWebServer()
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        stopKoin()
        server.shutdown()
    }

    @Test
    @Throws(Exception::class)
    fun testListCommitsSuccessful() = runBlocking {
        server.enqueue(
            MockResponse().setBody("/api/listCommits_success.json".loadResourceAsString())
        )
        server.start()

        val api = buildApi(server)
        val goodResponse = api.listCommits("test_user", "test_repository")

        val serverRequest = server.takeRequest()
        assertEquals("GET", serverRequest.method)
        assertEquals("/repos/test_user/test_repository/commits", serverRequest.path)

        assertTrue(goodResponse.errorBody() == null)
        assertTrue(goodResponse.isSuccessful)

        val commits = goodResponse.body()
        assertEquals(1, commits!!.size.toLong())
        val commit = commits[0]
        assertEquals("test message", commit.commitMessage)
        assertEquals("test author", commit.author)
    }

    @Test
    @Throws(Exception::class)
    fun testListCommitsUnsuccessful() = runBlocking {
        server.enqueue(MockResponse().setResponseCode(404).setBody("{\"message\": \"Not Found\"}"))
        server.start()

        val api = buildApi(server)
        val badResponse = api.listCommits("test_user", "test_repository")

        assertFalse(badResponse.isSuccessful)
        assertEquals(404, badResponse.code().toLong())
    }

    @Test(expected = UnknownHostException::class)
    @Throws(Exception::class)
    fun testListCommitsNetworkError(): Unit = runBlocking {
        val api = buildApi("http://bad_url/")
        api.listCommits("test_user", "test_repository")
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
//        val retrofit = module.provideRetrofit(client, baseUrl, converterFactory)
//        return module.provideGitHubApiService(retrofit)
        val retrofit = provideRetrofit(client, baseUrl, converterFactory)
        return provideGitHubApiService(retrofit)
    }
}
