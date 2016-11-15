package com.mycompany.myapp.ui.main;

import com.mycompany.myapp.EspressoTestRule;
import com.mycompany.myapp.MainApplicationDaggerMockRule;
import com.mycompany.myapp.R;
import com.mycompany.myapp.data.api.github.GitHubService;
import com.mycompany.myapp.data.api.github.GitHubService.LoadCommitsRequest;
import com.mycompany.myapp.data.api.github.GitHubService.LoadCommitsResponse;
import com.mycompany.myapp.data.api.github.model.Commit;
import com.squareup.spoon.Spoon;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import rx.Observable;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static com.mycompany.myapp.RecyclerViewMatcher.withRecyclerView;
import static org.mockito.Mockito.*;

public class MainActivityEspressoTest {

    @Rule public MainApplicationDaggerMockRule mockitoRule = new MainApplicationDaggerMockRule();

    @Rule public EspressoTestRule<MainActivity> activityRule = new EspressoTestRule<>(MainActivity.class, false, false);

    @Mock GitHubService gitHubService;

    @Test
    public void testBuildFingerprint() {
        when(gitHubService.loadCommits(any())).thenReturn(Observable.empty());

        activityRule.launchActivity(null);
        onView(withId(R.id.fingerprint)).check(matches(withText(new BaseMatcher<String>() {
            @Override
            public boolean matches(Object item) {
                Pattern pattern = Pattern.compile("Fingerprint: .+");
                return pattern.matcher(item.toString()).matches();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("regex 'Fingerprint: .+'");
            }
        })));
    }

    @Test
    public void testFetchAndDisplayCommits() {
        Observable<LoadCommitsResponse> response = buildMockLoadCommitsResponse();
        when(gitHubService.loadCommits(any())).thenReturn(response);

        MainActivity activity = activityRule.launchActivity(null);
        Spoon.screenshot(activity, "before_fetching_commits");

        onView(withId(R.id.fetch_commits)).perform(click());
        closeSoftKeyboard();

        Spoon.screenshot(activity, "after_fetching_commits");

        onView(withRecyclerView(R.id.commits)
                .atPositionOnView(0, R.id.author))
                .check(matches(withText("Test author")));
        onView(withRecyclerView(R.id.commits)
                .atPositionOnView(0, R.id.message))
                .check(matches(withText("Test commit message")));
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
