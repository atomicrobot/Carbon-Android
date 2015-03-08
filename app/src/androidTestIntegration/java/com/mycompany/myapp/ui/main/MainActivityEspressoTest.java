package com.mycompany.myapp.ui.main;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;

import com.mycompany.myapp.R;
import com.mycompany.myapp.data.api.github.GitHubBusService;
import com.mycompany.myapp.data.api.github.GitHubBusService.LoadCommitsRequest;
import com.mycompany.myapp.data.api.github.GitHubBusService.LoadCommitsResponse;
import com.mycompany.myapp.data.api.github.model.Commit;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@LargeTest
public class MainActivityEspressoTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private MainTestInjections testInjections;

    private MainActivity activity;

    public MainActivityEspressoTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        activity = getActivity();

        testInjections = new MainTestInjections();
        activity.getComponent().inject(testInjections);
    }

    public void testBuildFingerprint() {
        onView(withId(R.id.fingerprint)).check(matches(withText("Fingerprint: DEV")));
    }

    public void testFetchAndDisplayCommits() {
        GitHubBusService gitHubBusService = testInjections.gitHubBusService;
        final LoadCommitsRequest request = new LoadCommitsRequest("madebyatomicrobot", "android-starter-project");

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                Commit commit = mock(Commit.class);
                when(commit.getAuthor()).thenReturn("Test author");
                when(commit.getCommitMessage()).thenReturn("Test commit message");

                List<Commit> commits = new ArrayList<>();
                commits.add(commit);

                LoadCommitsResponse response = new LoadCommitsResponse(request, commits);
                testInjections.bus.post(response);
                return null;
            }
        }).when(gitHubBusService).loadCommits(any(LoadCommitsRequest.class));

        onView(withId(R.id.fetch_commits)).perform(click());

        onView(withId(R.id.author)).check(matches(withText("Author: Test author")));
        onView(withId(R.id.message)).check(matches(withText("Test commit message")));
    }
}
