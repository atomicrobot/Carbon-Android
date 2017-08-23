package com.mycompany.myapp.ui.main

import com.mycompany.myapp.data.api.github.GitHubService
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
class MainPresenterTest {

    @Mock private lateinit var githubService: GitHubService
    private lateinit var presenter: MainPresenter

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        val context = RuntimeEnvironment.application
        presenter = MainPresenter(context, githubService, Schedulers.trampoline(), Schedulers.trampoline(), 0)
    }

    @Test
    fun testGetVersion() {
        // CI systems can change the build number so we are a little more flexible on what to expect
        val expectedPattern = "Version: 1.0 b[1-9][0-9]*".toRegex()
        assertTrue("Version: 1.0 b123".matches(expectedPattern))
        assertTrue(presenter.getVersion().matches(expectedPattern))
    }

    @Test
    fun testGetFingerprint() {
        // CI systems can change the build number so we are a little more flexible on what to expect
        val expectedPattern = "Fingerprint: [a-zA-Z0-9]+".toRegex()
        assertTrue("Fingerprint: 0569b5cd8".matches(expectedPattern))
        assertTrue(presenter.getFingerprint().matches(expectedPattern))
    }

    @Test
    fun testFetchCommitsEnabled() {
        presenter.username = null
        presenter.repository = null
        assertFalse(presenter.isFetchCommitsEnabled())

        presenter.username = "test"
        presenter.repository = null
        assertFalse(presenter.isFetchCommitsEnabled())

        presenter.username = null
        presenter.repository = "test"
        assertFalse(presenter.isFetchCommitsEnabled())

        presenter.username = ""
        presenter.repository = ""
        assertFalse(presenter.isFetchCommitsEnabled())

        presenter.username = "test"
        presenter.repository = ""
        assertFalse(presenter.isFetchCommitsEnabled())

        presenter.username = ""
        presenter.repository = "test"
        assertFalse(presenter.isFetchCommitsEnabled())

        presenter.username = "test"
        presenter.repository = "test"
        assertTrue(presenter.isFetchCommitsEnabled())
    }
}