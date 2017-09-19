package com.mycompany.myapp.data.api.github

import com.mycompany.myapp.data.api.github.GitHubInteractor.LoadCommitsRequest
import com.mycompany.myapp.data.api.github.GitHubInteractor.LoadCommitsResponse
import com.mycompany.myapp.data.api.github.model.CommitTestHelper.stubCommit
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import retrofit2.Response
import java.util.Arrays.asList
import java.util.concurrent.TimeUnit

@RunWith(RobolectricTestRunner::class)
class GitHubInteractorTest {

    @Mock lateinit var api: GitHubApiService

    private lateinit var interactor: GitHubInteractor

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        val context = RuntimeEnvironment.application
        interactor = GitHubInteractor(context, api)
    }

    @Test
    @Throws(Exception::class)
    fun testLoadCommits() {
        val mockResponse = Single.just(Response.success(asList(stubCommit("test name", "test message"))))
        whenever(api.listCommits(anyString(), anyString())).thenReturn(mockResponse)

        val subscriber = TestObserver<LoadCommitsResponse>()
        interactor.loadCommits(LoadCommitsRequest("user", "repo")).subscribeWith(subscriber)
        subscriber.await(1, TimeUnit.SECONDS)

        subscriber.assertValueCount(1)
        subscriber.assertNoErrors()
        subscriber.assertComplete()

        val response = subscriber.values()[0]
        assertEquals("user", response.request.user)
        assertEquals("repo", response.request.repository)
        assertEquals(1, response.commits.size.toLong())

        val commit = response.commits[0]
        assertEquals("test name", commit.author)
        assertEquals("test message", commit.commitMessage)
    }
}