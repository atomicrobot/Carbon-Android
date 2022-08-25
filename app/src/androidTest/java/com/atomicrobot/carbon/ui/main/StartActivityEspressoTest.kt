package com.atomicrobot.carbon.ui.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.atomicrobot.carbon.data.DataModule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.runner.RunWith

@HiltAndroidTest
@UninstallModules(DataModule::class) // HERE
@RunWith(AndroidJUnit4::class)
class StartActivityEspressoTest {

    // TODO Fix for Compose
    // @get:Rule(order = 0)
    // var hiltRule = HiltAndroidRule(this)
    //
    // @get:Rule(order = 1)
    // var testRule: ActivityScenarioRule<StartActivity> =
    //     ActivityScenarioRule(StartActivity::class.java)
    //
    // @Before
    // fun setUp() {
    //     hiltRule.inject()
    // }
    //
    // @InstallIn(SingletonComponent::class)
    // @Module
    // object TestDataModule {
    //
    //     @Provides
    //     @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    //     fun provideGitHubApiService(): GitHubApiService {
    //         return TestGitHubService()
    //     }
    //
    //     @Provides
    //     fun provideGitHubService(
    //         @ApplicationContext context: Context,
    //         api: GitHubApiService
    //     ): GitHubInteractor {
    //         return GitHubInteractor(context, api)
    //     }
    // }
    //
    // @Test
    // fun testBuildFingerprint() {
    //     onView(withId(R.id.fingerprint)).check(matches(withText(regex("Fingerprint: .+"))))
    // }
    //
    // @Test
    // fun testFetchCommitsEnabledState() {
    //     onView(withId(R.id.username)).perform(clearText())
    //     onView(withId(R.id.fetch_commits)).check(matches(not(isEnabled())))
    //
    //     onView(withId(R.id.username)).perform(typeText("username"))
    //     onView(withId(R.id.fetch_commits)).check(matches(isEnabled()))
    //
    //     onView(withId(R.id.repository)).perform(clearText())
    //     onView(withId(R.id.fetch_commits)).check(matches(not(isEnabled())))
    // }
    //
    // @Test
    // fun testFetchAndDisplayCommits() {
    //
    //     onView(withId(R.id.username)).perform(clearText())
    //     onView(withId(R.id.username)).perform(typeText("atomicrobot"))
    //
    //     onView(withId(R.id.repository)).perform(clearText())
    //     onView(withId(R.id.repository)).perform(typeText("android-starter-project"))
    //
    //     closeSoftKeyboard()
    //
    //     onView(
    //         withRecyclerView(R.id.commits)
    //             .atPositionOnView(0, R.id.author)
    //     )
    //         .check(matches(withText("Author: Test author")))
    //     onView(
    //         withRecyclerView(R.id.commits)
    //             .atPositionOnView(0, R.id.message)
    //     )
    //         .check(matches(withText("Test commit message")))
    // }
}
