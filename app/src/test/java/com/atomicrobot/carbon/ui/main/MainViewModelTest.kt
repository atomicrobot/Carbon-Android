package com.atomicrobot.carbon.ui.main

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.atomicrobot.carbon.data.api.github.GitHubInteractor
import com.atomicrobot.carbon.data.api.github.model.Commit
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
class MainViewModelTest {

    @Mock
    private lateinit var githubInteractor: GitHubInteractor
    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)

        val app = ApplicationProvider.getApplicationContext<Application>()
        viewModel = MainViewModel(
            app,
            githubInteractor
        )
    }

    @Test
    fun testFetchCommits() = runBlocking {
        val mockResult = mock(GitHubInteractor.LoadCommitsResponse::class.java)
        val mockCommit = mock(Commit::class.java)
        whenever(mockResult.commits).thenReturn(listOf(mockCommit))
        whenever(githubInteractor.loadCommits(any())).thenReturn(mockResult)

        assertTrue(
            (
                viewModel.uiState.value.commitsState as?
                    MainViewModel.Commits.Result
                )?.commits?.isEmpty()
                ?: false
        )
        viewModel.fetchCommits()
        assertTrue(
            (
                viewModel.uiState.value.commitsState as?
                    MainViewModel.Commits.Result
                )?.commits?.size == 1
        )
    }

    @Test
    fun testGetVersion() {
        // CI systems can change the build number so we are a little more flexible on what to expect
        val expectedPattern = "1.0 b[1-9][0-9]*".toRegex()
        assertTrue("1.0 b123".matches(expectedPattern))
        assertTrue(viewModel.getVersion().matches(expectedPattern))
    }

    @Test
    fun testGetVersionFingerprint() {
        val expectedPattern = "[a-zA-Z0-9]+".toRegex()
        assertTrue("0569b5cd8".matches(expectedPattern))
        assertTrue("DEV".matches(expectedPattern))
        assertTrue(viewModel.getVersionFingerprint().matches(expectedPattern))
    }
}
