package com.mycompany.myapp.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mycompany.myapp.BuildConfig;
import com.mycompany.myapp.R;
import com.mycompany.myapp.data.api.github.GitHubService;
import com.mycompany.myapp.data.api.github.GitHubService.LoadCommitsRequest;
import com.mycompany.myapp.data.api.github.model.Commit;
import com.mycompany.myapp.util.RxUtils;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainPresenter {
    private static final String EXTRA_STATE = "MainPresenterState";

    public interface MainViewContract {
        void displayUsername(String username);

        void displayRepository(String repository);

        void displayCommits(List<CommitViewModel> commits);

        void displayError(String message);

        void displayVersion(String version);

        void displayFingerprint(String fingerprint);
    }

    @Parcel
    public static class CommitViewModel {
        final String message;
        final String author;

        @ParcelConstructor
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

    @Parcel
    public static class State {
        boolean initialized = false;
        String username = "madebyatomicrobot";
        String repository = "android-starter-project";
        List<CommitViewModel> commits = new ArrayList<>();
    }

    private final Context context;
    private final GitHubService gitHubService;

    private CompositeDisposable disposables;
    private MainViewContract view;
    private State state;

    public MainPresenter(Context context, GitHubService gitHubService) {
        this.context = context;
        this.gitHubService = gitHubService;
        this.state = new State();
    }

    public void setView(MainViewContract viewContract) {
        this.view = viewContract;
    }

    public void saveState(@NonNull Bundle bundle) {
        bundle.putParcelable(EXTRA_STATE, Parcels.wrap(state));
    }

    public void restoreState(@Nullable Bundle bundle) {
        if (bundle != null && bundle.containsKey(EXTRA_STATE)) {
            state = Parcels.unwrap(bundle.getParcelable(EXTRA_STATE));
        }
    }

    public void onResume() {
        disposables = RxUtils.getNewCompositeDisposableIfDisposed(disposables);
        if (!state.initialized) {
            // Do initial setup here
            state.initialized = true;
        }

        view.displayUsername(state.username);
        view.displayRepository(state.repository);
        view.displayCommits(state.commits);
        view.displayVersion(String.format("Version: %s", BuildConfig.VERSION_NAME));
        view.displayFingerprint(String.format("Fingerprint: %s", BuildConfig.VERSION_FINGERPRINT));

        fetchCommits();
    }

    public void onPause() {
        RxUtils.disposeIfNotNull(disposables);
    }

    public void setUsername(String username) {
        state.username = username;
    }

    public void setRepository(String repository) {
        state.repository = repository;
    }

    public void fetchCommits() {
        disposables.add(loadCommits(buildLoadCommitsRequest()));
    }

    private LoadCommitsRequest buildLoadCommitsRequest() {
        return new LoadCommitsRequest(state.username, state.repository);
    }

    private Disposable loadCommits(LoadCommitsRequest request) {
        return gitHubService.loadCommits(request)
                .flatMap(response -> Observable.fromIterable(response.getCommits()))
                .map(this::mapCommitToViewModel)
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleLoadCommitsResponse, this::handleError);
    }

    private CommitViewModel mapCommitToViewModel(Commit commit) {
        String message = commit.getCommitMessage();
        String author = context.getString(R.string.author_format, commit.getAuthor());
        return new CommitViewModel(message, author);
    }

    private void handleLoadCommitsResponse(List<CommitViewModel> commits) {
        state.commits = commits;
        view.displayCommits(commits);
    }

    private void handleError(Throwable throwable) {
        String message = throwable.getMessage();
        view.displayError(message);
    }
}
