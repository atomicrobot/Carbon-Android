package com.mycompany.myapp.ui.main;

import android.content.Context;

import com.mycompany.myapp.data.api.github.GitHubService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class MainPresenterTest {

    @Mock GitHubService githubService;

    private MainPresenter presenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        Context context = RuntimeEnvironment.application;
        presenter = new MainPresenter(context, githubService);
    }

    @Test
    public void testGetVersion() {
        assertEquals("Version: 1.0 b1", presenter.getVersion());
    }

    @Test
    public void testGetFingerprint() {
        assertEquals("Fingerprint: DEV", presenter.getFingerprint());
    }

    @Test
    public void testFetchCommitsEnabled() {
        presenter.setUsername(null);
        presenter.setRepository(null);
        assertFalse(presenter.isFetchCommitsEnabled());

        presenter.setUsername("test");
        presenter.setRepository(null);
        assertFalse(presenter.isFetchCommitsEnabled());

        presenter.setUsername(null);
        presenter.setRepository("test");
        assertFalse(presenter.isFetchCommitsEnabled());

        presenter.setUsername("");
        presenter.setRepository("");
        assertFalse(presenter.isFetchCommitsEnabled());

        presenter.setUsername("test");
        presenter.setRepository("");
        assertFalse(presenter.isFetchCommitsEnabled());

        presenter.setUsername("");
        presenter.setRepository("test");
        assertFalse(presenter.isFetchCommitsEnabled());

        presenter.setUsername("test");
        presenter.setRepository("test");
        assertTrue(presenter.isFetchCommitsEnabled());
    }
}