package com.mycompany.myapp.ui.main;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.mycompany.myapp.R;
import com.mycompany.myapp.data.api.github.GitHubBusService;
import com.mycompany.myapp.data.api.github.GitHubBusService.LoadCommitsRequest;
import com.mycompany.myapp.data.api.github.GitHubBusService.LoadCommitsResponse;
import com.mycompany.myapp.data.api.github.model.Commit;
import com.squareup.otto.Bus;
import com.squareup.spoon.Spoon;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityEspressoTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class, true);

    @Test
    public void testBuildFingerprint() {
        onView(withId(R.id.fingerprint)).check(matches(withText("Fingerprint: DEV")));
    }

    @Test
    public void testFetchAndDisplayCommits() {
        MainActivity activity = activityRule.getActivity();
        final Bus bus = activity.getComponent().bus();
        final GitHubBusService gitHubBusService = activity.getComponent().gitHubBusService();

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
                bus.post(response);

                return null;
            }
        }).when(gitHubBusService).loadCommits(any(LoadCommitsRequest.class));

        Spoon.screenshot(activity, "before_fetching_commits");

        onView(withId(R.id.fetch_commits)).perform(click());
        closeSoftKeyboard();

        // TODO - This is a hack...
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        Spoon.screenshot(activity, "after_fetching_commits");

        onView(withId(R.id.author)).check(matches(withText("Author: Test author")));
        onView(withId(R.id.message)).check(matches(withText("Test commit message")));
    }
}
