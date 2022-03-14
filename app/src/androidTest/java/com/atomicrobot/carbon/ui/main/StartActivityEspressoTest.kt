package com.atomicrobot.carbon.ui.main

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.atomicrobot.carbon.EspressoMatchers.regex
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.StartActivity
import com.atomicrobot.carbon.data.api.github.GitHubApiService
import com.atomicrobot.carbon.data.api.github.GitHubInteractor
import com.atomicrobot.carbon.data.api.github.GitHubInteractor.LoadCommitsRequest
import com.atomicrobot.carbon.data.api.github.GitHubInteractor.LoadCommitsResponse
import com.atomicrobot.carbon.data.api.github.model.Author
import com.atomicrobot.carbon.data.api.github.model.Commit
import com.atomicrobot.carbon.data.api.github.model.CommitDetails
import com.atomicrobot.carbon.withRecyclerView
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
class StartActivityEspressoTest {

    @get:Rule val testRule = ActivityScenarioRule(StartActivity::class.java)

    @Mock lateinit var gitHubInteractor: GitHubInteractor
    @Mock lateinit var api: GitHubApiService

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        val context = ApplicationProvider.getApplicationContext<Application>()
        gitHubInteractor = GitHubInteractor(context, api)
    }

    @Test
    fun testBuildFingerprint() {
        whenever(gitHubInteractor.loadCommits(any())).thenReturn(Observable.empty())

        onView(withId(R.id.fingerprint)).check(matches(withText(regex("Fingerprint: .+"))))
    }

    @Test
    fun testFetchCommitsEnabledState() {
        val response = LoadCommitsResponse(
                LoadCommitsRequest("username", "repository"),
                emptyList())
        whenever(gitHubInteractor.loadCommits(any())).thenReturn(Observable.just(response))

        onView(withId(R.id.fetch_commits)).check(matches(isEnabled()))

        onView(withId(R.id.username)).perform(clearText())
        onView(withId(R.id.fetch_commits)).check(matches(not(isEnabled())))

        onView(withId(R.id.username)).perform(typeText("username"))
        onView(withId(R.id.fetch_commits)).check(matches(isEnabled()))

        onView(withId(R.id.repository)).perform(clearText())
        onView(withId(R.id.fetch_commits)).check(matches(not(isEnabled())))
    }

    @Test
    fun testFetchAndDisplayCommits() {
        val response = buildMockLoadCommitsResponse()
        whenever(gitHubInteractor.loadCommits(any())).thenReturn(response)

        closeSoftKeyboard()

        onView(withRecyclerView(R.id.commits)
                .atPositionOnView(0, R.id.author))
                .check(matches(withText("Author: Test author")))
        onView(withRecyclerView(R.id.commits)
                .atPositionOnView(0, R.id.message))
                .check(matches(withText("Test commit message")))
    }

    private fun buildMockLoadCommitsResponse(): Observable<LoadCommitsResponse> {
        val request = LoadCommitsRequest("madebyatomicrobot", "android-starter-project")
        val commit = Commit(CommitDetails("Test commit message", Author("Test author")))
        return Observable.just(LoadCommitsResponse(request, listOf(commit)))
    }
}
