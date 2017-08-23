package com.mycompany.myapp.ui.main;

import android.content.Context;

import com.mycompany.myapp.data.api.github.GitHubService;
import com.mycompany.myapp.data.api.github.GitHubService.LoadCommitsRequest;
import com.mycompany.myapp.data.api.github.GitHubService.LoadCommitsResponse;
import com.mycompany.myapp.data.api.github.model.Commit;
import com.mycompany.myapp.ui.main.MainPresenter.CommitView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(RobolectricTestRunner.class)
public class MainPresenterTest {

    @Mock GitHubService githubService;

    private MainPresenter presenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        Context context = RuntimeEnvironment.application;
        presenter = new MainPresenter(
                context,
                githubService,
                Schedulers.trampoline(),
                Schedulers.trampoline());
    }

    @Test
    public void testGetVersion() {
        // CI systems can change the build number so we are a little more flexible on what to expect
        final String expectedPattern = "Version: 1.0 b[1-9][0-9]*";
        assertTrue("Version: 1.0 b123".matches(expectedPattern));
        assertTrue(presenter.getVersion().matches(expectedPattern));
    }

    @Test
    public void testGetFingerprint() {
        // CI systems can change the build number so we are a little more flexible on what to expect
        final String expectedPattern = "Fingerprint: [a-zA-Z0-9]+";
        assertTrue("Fingerprint: 0569b5cd8".matches(expectedPattern));
        assertTrue(presenter.getFingerprint().matches(expectedPattern));
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

    @Test
    public void testFetchCommits() {
        Commit commit = mock(Commit.class);
        when(commit.getAuthor()).thenReturn("test");
        when(commit.getCommitMessage()).thenReturn("test message");
        List<Commit> commits = Collections.singletonList(commit);
        LoadCommitsResponse response = mock(LoadCommitsResponse.class);
        when(response.getCommits()).thenReturn(commits);
        Observable<LoadCommitsResponse> observable = Observable.fromArray(response);
        when(githubService.loadCommits(any(LoadCommitsRequest.class))).thenReturn(observable);

        presenter.setUsername("test");
        presenter.setRepository("test");
        presenter.setCommits(Collections.emptyList());
        presenter.fetchCommits();

        assertEquals(1, presenter.getState().commits.size());
        CommitView commitView = presenter.getState().commits.get(0);
        assertEquals("Author: test", commitView.getAuthor());
        assertEquals("test message", commitView.getMessage());
    }
}