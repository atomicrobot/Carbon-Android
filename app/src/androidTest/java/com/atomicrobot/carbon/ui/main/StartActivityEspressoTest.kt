package com.atomicrobot.carbon.ui.main

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.atomicrobot.carbon.EspressoMatchers.regex
import com.atomicrobot.carbon.R
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
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.GlobalContext.loadKoinModules
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.dsl.module
import org.mockito.Mockito

@RunWith(AndroidJUnit4::class)
class StartActivityEspressoTest {
    private lateinit var scenario: ActivityScenario<StartActivity>

//    @get:Rule val activityRule = ActivityScenarioRule(StartActivity::class.java)

    private lateinit var gitHubInteractor: GitHubInteractor

    @Before
    fun setup() {
        gitHubInteractor = Mockito.mock(GitHubInteractor::class.java)
        loadKoinModules(module {
            gitHubInteractor
        })
    }

    @After
    fun cleanUp() {
        stopKoin()
        scenario.close()
    }

    @Test
    fun testBuildFingerprint() {
        whenever(gitHubInteractor.loadCommits(any())).thenReturn(Observable.empty())
        scenario = ActivityScenario.launch(StartActivity::class.java)
        onView(withId(R.id.fingerprint)).check(matches(withText(regex("Fingerprint: .+"))))
    }

    @Test
    fun testFetchCommitsEnabledState() {
        val response = LoadCommitsResponse(
                LoadCommitsRequest("username", "repository"),
                emptyList())
        whenever(gitHubInteractor.loadCommits(any())).thenReturn(Observable.just(response))

        scenario = ActivityScenario.launch(StartActivity::class.java)

        Thread.sleep(5 * 1000L)
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()

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

        scenario = ActivityScenario.launch(StartActivity::class.java)

        Thread.sleep(5 * 1000L)
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()

        closeSoftKeyboard()

        // Check recycler view's 0th position and confirm text does not equal default test values
        onView(withRecyclerView(R.id.commits)
            .atPositionOnView(0, R.id.author))
            .check(matches(not(withText("Author: Test author"))))
        onView(withRecyclerView(R.id.commits)
            .atPositionOnView(0, R.id.message))
            .check(matches(not(withText("Test commit message"))))
    }

    private fun buildMockLoadCommitsResponse(): Observable<LoadCommitsResponse> {
        val request = LoadCommitsRequest("madebyatomicrobot", "android-starter-project")
        val commit = Commit(CommitDetails("Test commit message", Author("Test author")))
        return Observable.just(LoadCommitsResponse(request, listOf(commit)))
    }
}
