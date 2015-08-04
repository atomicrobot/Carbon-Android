package com.mycompany.myapp.ui.main;

import com.mycompany.myapp.BuildConfig;
import com.mycompany.myapp.R;
import com.mycompany.myapp.data.api.github.GitHubService;
import com.mycompany.myapp.data.api.github.GitHubService.LoadCommitsRequest;
import com.mycompany.myapp.data.api.github.model.Commit;
import com.mycompany.myapp.ui.Resource;
import com.mycompany.myapp.util.RxUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class MainPresenter {
    public interface MainViewContract {
        void displayUsername(String username);

        void displayRepository(String repository);

        void displayCommits(List<CommitViewModel> commits);

        void displayError(String message);

        void displayVersion(String version);

        void displayFingerprint(String fingerprint);
    }

    public static class CommitViewModel {
        private final String message;
        private final String author;

        public CommitViewModel(String message, String author) {
            this.message = message;
            this.author = author;
        }

        public String getMessage() {
            return message;
        }

        public String getAuthor() {
            return author;
        }
    }

    private class SavedState {
        private String username = "madebyatomicrobot";
        private String repository = "android-starter-project";
        private List<CommitViewModel> commits = new ArrayList<>();
    }

    private final GitHubService gitHubService;

    private final String authorFormat;

    private SavedState savedState = new SavedState();

    private MainViewContract view;

    private CompositeSubscription subscriptions;

    @Inject
    public MainPresenter(GitHubService gitHubService, @Resource(R.string.author_format) String authorFormat) {
        this.gitHubService = gitHubService;
        this.authorFormat = authorFormat;
    }

    public void setView(MainViewContract view) {
        this.view = view;
    }

    public void onResume() {
        subscriptions = RxUtils.getNewCompositeSubIfUnsubscribed(subscriptions);

        view.displayUsername(savedState.username);
        view.displayRepository(savedState.repository);
        view.displayCommits(savedState.commits);
        view.displayVersion(String.format("Version: %s", BuildConfig.VERSION_NAME));
        view.displayFingerprint(String.format("Fingerprint: %s", BuildConfig.VERSION_FINGERPRINT));

        fetchCommits();
    }

    public void onPause() {
        RxUtils.unsubscribeIfNotNull(subscriptions);
    }

    public void setUsername(String username) {
        savedState.username = username;
    }

    public void setRepository(String repository) {
        savedState.repository = repository;
    }

    public void fetchCommits() {
        subscriptions.add(loadCommits(buildLoadCommitsRequest()));
    }

    private LoadCommitsRequest buildLoadCommitsRequest() {
        return new LoadCommitsRequest(savedState.username, savedState.repository);
    }

    private Subscription loadCommits(LoadCommitsRequest request) {
        return gitHubService.loadCommits(request)
                .flatMap(response -> Observable.from(response.getCommits()))
                .map(this::mapCommitToViewModel)
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleLoadCommitsResponse, this::handleError);
    }

    private CommitViewModel mapCommitToViewModel(Commit commit) {
        String message = commit.getCommitMessage();
        String author = String.format(authorFormat, commit.getAuthor());
        return new CommitViewModel(message, author);
    }

    private void handleLoadCommitsResponse(List<CommitViewModel> commits) {
        savedState.commits = commits;
        view.displayCommits(commits);
    }

    private void handleError(Throwable throwable) {
        view.displayError(throwable.getMessage());
    }
}
