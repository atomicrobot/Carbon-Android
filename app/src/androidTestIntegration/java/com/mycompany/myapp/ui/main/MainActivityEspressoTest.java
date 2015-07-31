package com.mycompany.myapp.ui.main;

import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.MediumTest;

import com.mycompany.myapp.R;
import com.mycompany.myapp.app.ApplicationComponent;
import com.mycompany.myapp.app.MainApplication;
import com.mycompany.myapp.data.api.github.GitHubService;
import com.mycompany.myapp.data.api.github.GitHubService.LoadCommitsRequest;
import com.mycompany.myapp.data.api.github.GitHubService.LoadCommitsResponse;
import com.mycompany.myapp.data.api.github.model.Commit;
import com.squareup.spoon.Spoon;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
@MediumTest
public class MainActivityEspressoTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class, true, false);

    private GitHubService getMockGitHubService() {
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        Context targetContext = instrumentation.getTargetContext();
        Application application = (Application) targetContext.getApplicationContext();

        MainApplication mainApplication = (MainApplication) application;
        ApplicationComponent applicationComponent = mainApplication.getComponent();
        return applicationComponent.gitHubService();
    }

    @Test
    public void testBuildFingerprint() {
        GitHubService gitHubService = getMockGitHubService();
        when(gitHubService.loadCommits(any())).thenReturn(Observable.<LoadCommitsResponse>empty());

        MainActivity activity = activityRule.launchActivity(null);
        onView(withId(R.id.fingerprint)).check(matches(withText("Fingerprint: DEV")));
    }

    @Test
    public void testFetchAndDisplayCommits() {
        GitHubService gitHubService = getMockGitHubService();
        Observable<LoadCommitsResponse> response = buildMockLoadCommitsResponse();
        when(gitHubService.loadCommits(any())).thenReturn(response);

        MainActivity activity = activityRule.launchActivity(null);
        Spoon.screenshot(activity, "before_fetching_commits");

        onView(withId(R.id.fetch_commits)).perform(click());
        closeSoftKeyboard();

        Spoon.screenshot(activity, "after_fetching_commits");

        onView(withId(R.id.author)).check(matches(withText("Author: Test author")));
        onView(withId(R.id.message)).check(matches(withText("Test commit message")));
    }

    private Observable<LoadCommitsResponse> buildMockLoadCommitsResponse() {
        Commit commit = mock(Commit.class);
        when(commit.getAuthor()).thenReturn("Test author");
        when(commit.getCommitMessage()).thenReturn("Test commit message");

        List<Commit> commits = new ArrayList<>();
        commits.add(commit);

        LoadCommitsRequest request = new LoadCommitsRequest("madebyatomicrobot", "android-starter-project");
        return Observable.just(new LoadCommitsResponse(request, commits));
    }
}
