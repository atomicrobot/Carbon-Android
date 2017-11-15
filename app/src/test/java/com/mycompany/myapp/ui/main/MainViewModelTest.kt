package com.mycompany.myapp.ui.main

import com.mycompany.myapp.data.api.github.GitHubInteractor
import io.reactivex.schedulers.Schedulers
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class MainViewModelTest {

    @Mock private lateinit var githubInteractor: GitHubInteractor

    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        val context = RuntimeEnvironment.application
        viewModel = MainViewModel(
                context,
                githubInteractor,
                Schedulers.trampoline(),
                Schedulers.trampoline(),
                0)
    }

    @Test
    fun testGetVersion() {
        // CI systems can change the build number so we are a little more flexible on what to expect
        val expectedPattern = "Version: 1.0 b[1-9][0-9]*".toRegex()
        assertTrue("Version: 1.0 b123".matches(expectedPattern))
        assertTrue(viewModel.getVersion().matches(expectedPattern))
    }

    @Test
    fun testGetFingerprint() {
        // CI systems can change the build number so we are a little more flexible on what to expect
        val expectedPattern = "Fingerprint: [a-zA-Z0-9]+".toRegex()
        assertTrue("Fingerprint: 0569b5cd8".matches(expectedPattern))
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
}