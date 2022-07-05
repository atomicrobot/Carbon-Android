package com.atomicrobot.carbon.ui.main

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.atomicrobot.carbon.EspressoMatchers.regex
import com.atomicrobot.carbon.data.DataModule
import com.atomicrobot.carbon.data.TestGitHubService
import com.atomicrobot.carbon.data.api.github.GitHubApiService
import com.atomicrobot.carbon.data.api.github.GitHubInteractor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@UninstallModules(DataModule::class) // HERE
@RunWith(AndroidJUnit4::class)
class StartActivityEspressoTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var testRule: ActivityScenarioRule<StartActivity> =
        ActivityScenarioRule(StartActivity::class.java)

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @InstallIn(SingletonComponent::class)
    @Module
    object TestDataModule {

        @Provides
        @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
        fun provideGitHubApiService(): GitHubApiService {
            return TestGitHubService()
        }

        @Provides
        fun provideGitHubService(
            @ApplicationContext context: Context,
            api: GitHubApiService
        ): GitHubInteractor {
            return GitHubInteractor(context, api)
        }
    }

    @Test
    fun testBuildFingerprint() {
        onView(withId(R.id.fingerprint)).check(matches(withText(regex("Fingerprint: .+"))))
    }

    @Test
    fun testFetchCommitsEnabledState() {
        onView(withId(R.id.username)).perform(clearText())
        onView(withId(R.id.fetch_commits)).check(matches(not(isEnabled())))

        onView(withId(R.id.username)).perform(typeText("username"))
        onView(withId(R.id.fetch_commits)).check(matches(isEnabled()))

        onView(withId(R.id.repository)).perform(clearText())
        onView(withId(R.id.fetch_commits)).check(matches(not(isEnabled())))
    }

    @Test
    fun testFetchAndDisplayCommits() {

        onView(withId(R.id.username)).perform(clearText())
        onView(withId(R.id.username)).perform(typeText("atomicrobot"))

        onView(withId(R.id.repository)).perform(clearText())
        onView(withId(R.id.repository)).perform(typeText("android-starter-project"))

        closeSoftKeyboard()

        onView(
            withRecyclerView(R.id.commits)
                .atPositionOnView(0, R.id.author)
        )
            .check(matches(withText("Author: Test author")))
        onView(
            withRecyclerView(R.id.commits)
                .atPositionOnView(0, R.id.message)
        )
            .check(matches(withText("Test commit message")))
    }
}
