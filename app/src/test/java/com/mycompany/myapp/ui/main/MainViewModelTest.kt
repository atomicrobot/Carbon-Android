package com.mycompany.myapp.ui.main

import com.mycompany.myapp.TrampolineSchedulerRule
import com.mycompany.myapp.data.api.github.GitHubInteractor
import com.mycompany.myapp.data.api.github.model.Commit
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment


@RunWith(RobolectricTestRunner::class)
class MainViewModelTest {

    @JvmField @Rule val trampolineSchedulerRule = TrampolineSchedulerRule()

    @Mock private lateinit var githubInteractor: GitHubInteractor

    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        val app = RuntimeEnvironment.application
        viewModel = MainViewModel(
                app,
                githubInteractor,
                0)
    }

    @Test
    fun testGetVersion() {
        // CI systems can change the build number so we are a little more flexible on what to expect
        val expectedPattern = "1.0 b[1-9][0-9]*".toRegex()
        assertTrue("1.0 b123".matches(expectedPattern))
        assertTrue(viewModel.getVersion().matches(expectedPattern))
    }

    @Test
    fun testGetFingerprint() {
        // CI systems can change the build number so we are a little more flexible on what to expect
        val expectedPattern = "[a-zA-Z0-9]+".toRegex()
        assertTrue("0569b5cd8".matches(expectedPattern))
        assertTrue(viewModel.getFingerprint().matches(expectedPattern))
    }

    @Test
    fun testFetchCommitsEnabled() {
        viewModel.username = "test"
        viewModel.repository = ""
        assertFalse(viewModel.isFetchCommitsEnabled())

        viewModel.username = ""
        viewModel.repository = "test"
        assertFalse(viewModel.isFetchCommitsEnabled())

        viewModel.username = ""
        viewModel.repository = ""
        assertFalse(viewModel.isFetchCommitsEnabled())

        viewModel.username = "test"
        viewModel.repository = ""
        assertFalse(viewModel.isFetchCommitsEnabled())

        viewModel.username = ""
        viewModel.repository = "test"
        assertFalse(viewModel.isFetchCommitsEnabled())

        viewModel.username = "test"
        viewModel.repository = "test"
        assertTrue(viewModel.isFetchCommitsEnabled())
    }

    @Test
    fun testFetchCommits() {
        val mockResult = mock(GitHubInteractor.LoadCommitsResponse::class.java)
        val mockCommit = mock(Commit::class.java)
        whenever(mockResult.commits).thenReturn(listOf(mockCommit))
        whenever(githubInteractor.loadCommits(any())).thenReturn(Observable.just(mockResult))

        assertTrue((viewModel.commits as? MainViewModel.Commits.Result)?.commits?.isEmpty() ?: false)
        viewModel.fetchCommits()
        assertTrue((viewModel.commits as? MainViewModel.Commits.Result)?.commits?.size == 1)
    }
}