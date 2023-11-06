package com.atomicrobot.carbon.data.api.github

import com.atomicrobot.carbon.app.provideDetailedGitHubApiService
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

internal class GitHubCardApiServiceTest {
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
    fun testDetailedCommitSuccessful() = runBlocking {
        server.enqueue(
            MockResponse().setBody("/api/testDetailedCommit_success.json".loadResourceAsString())
        )
        server.start()

        val api = buildApi(server)

        val goodResponse = api.detailedCommit("test_user", "test_repository", "test_sha")

        val serverRequest = server.takeRequest()
        assertEquals("GET", serverRequest.method)
        assertEquals("/repos/test_user/test_repository/commits/test_sha", serverRequest.path)

        assertTrue(goodResponse.errorBody() == null)
        assertTrue(goodResponse.isSuccessful)

        val commit = goodResponse.body()
        assertEquals("test message", commit?.detailedCommitMessage)
        assertEquals("test author", commit?.detailedCommitAuthor)
        assertEquals("test/tree/url", commit?.detailedCommitTreeURL)
        assertEquals(true, commit?.detailedCommitVerified)
    }

    @Test
    @Throws(Exception::class)
    fun testListCommitsUnsuccessful() = runBlocking {
        server.enqueue(MockResponse().setResponseCode(404).setBody("{\"message\": \"Not Found\"}"))
        server.start()

        val api = buildApi(server)
        val badResponse = api.detailedCommit("test_user", "test_repository", "test_sha")

        assertFalse(badResponse.isSuccessful)
        assertEquals(404, badResponse.code().toLong())
    }

    @Test(expected = UnknownHostException::class)
    @Throws(Exception::class)
    fun testListCommitsNetworkError(): Unit = runBlocking {
        val api = buildApi("http://bad_url/")
        api.detailedCommit("test_user", "test_repository", "test_sha")
    }

    @Throws(Exception::class)
    private fun buildApi(server: MockWebServer): DetailedGitHubApiService {
        val baseUrl = server.url("")
        return buildApi(baseUrl.toString())
    }

    @Throws(Exception::class)
    private fun buildApi(baseUrl: String): DetailedGitHubApiService {
        val client = OkHttpClient.Builder().build()
        // TODO - fix with koin tests?
        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        val converterFactory = MoshiConverterFactory.create(moshi) as Converter.Factory
//      val retrofit = module.provideRetrofit(client, baseUrl, converterFactory)
//      return module.provideDetailedGitHubApiService(retrofit)
        val retrofit = provideRetrofit(client, baseUrl, converterFactory)
        return provideDetailedGitHubApiService(retrofit)
    }
}
