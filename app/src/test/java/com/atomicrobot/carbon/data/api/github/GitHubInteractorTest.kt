package com.atomicrobot.carbon.data.api.github

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.atomicrobot.carbon.data.api.github.GitHubInteractor.LoadCommitsRequest
import com.atomicrobot.carbon.data.api.github.model.CommitTestHelper.stubCommit
import com.nhaarman.mockito_kotlin.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import retrofit2.Response

@RunWith(AndroidJUnit4::class)
class GitHubInteractorTest {

    @Mock lateinit var api: GitHubApiService

    private lateinit var interactor: GitHubInteractor

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)

        val context = ApplicationProvider.getApplicationContext<Application>()
        interactor = GitHubInteractor(context, api)
    }

    @Test
    @Throws(Exception::class)
    fun testLoadCommits() = runBlocking {
        val mockResponse = Response.success(listOf(stubCommit("test name", "test message")))
        whenever(api.listCommits(anyString(), anyString())).thenReturn(mockResponse)

        val response = interactor.loadCommits(LoadCommitsRequest("user", "repo"))

        assertTrue(mockResponse.isSuccessful)
        assertEquals(mockResponse, interactor.checkResponse(mockResponse, ""))
        assertTrue(response.commits.isNotEmpty())

        assertEquals("user", response.request.user)
        assertEquals("repo", response.request.repository)
        assertEquals(1, response.commits.size.toLong())

        val commit = response.commits[0]
        assertEquals("test name", commit.author)
        assertEquals("test message", commit.commitMessage)
    }
}
