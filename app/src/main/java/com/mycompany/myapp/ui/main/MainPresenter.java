package com.mycompany.myapp.ui.main;

import android.content.Context;
import android.databinding.Bindable;
import android.text.TextUtils;

import com.mycompany.myapp.BR;
import com.mycompany.myapp.BuildConfig;
import com.mycompany.myapp.R;
import com.mycompany.myapp.data.api.github.GitHubService;
import com.mycompany.myapp.data.api.github.GitHubService.LoadCommitsRequest;
import com.mycompany.myapp.ui.BasePresenter;
import com.mycompany.myapp.ui.main.MainPresenter.MainViewContract;
import com.mycompany.myapp.ui.main.MainPresenter.State;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;

public class MainPresenter extends BasePresenter<MainViewContract, State> {
    private static final String STATE_KEY = "MainPresenterState";  // NON-NLS

    public interface MainViewContract {
        void displayError(String message);
    }

    @Parcel
    public static class CommitView {
        String message;
        String author;

        @ParcelConstructor
        CommitView(String message, String author) {
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

        String username;
        String repository;
        List<CommitView> commits;
    }

    private final Context context;
    private final GitHubService gitHubService;
    private final Scheduler ioScheduler;
    private final Scheduler mainScheduler;

    public MainPresenter(
            Context context,
            GitHubService gitHubService,
            Scheduler ioScheduler,
            Scheduler mainScheduler) {
        super(STATE_KEY);
        this.context = context;
        this.gitHubService = gitHubService;
        this.ioScheduler = ioScheduler;
        this.mainScheduler = mainScheduler;

        this.state = new State();
    }

    public void onResume() {
        super.onResume();
        if (!state.initialized) {
            state.initialized = true;
            setUsername("madebyatomicrobot");  // NON-NLS
            setRepository("android-starter-project");  // NON-NLS
            setCommits(new ArrayList<>());
        }

        fetchCommits();
    }

    @Bindable
    public String getUsername() {
        return state.username;
    }

    public void setUsername(String username) {
        state.username = username;
        notifyPropertyChanged(BR.username);
    }

    @Bindable
    public String getRepository() {
        return state.repository;
    }

    public void setRepository(String repository) {
        state.repository = repository;
        notifyPropertyChanged(BR.repository);
    }

    @Bindable
    public List<CommitView> getCommits() {
        return state.commits;
    }

    public void setCommits(List<CommitView> commits) {
        state.commits = commits;
        notifyPropertyChanged(BR.commits);
    }

    @Bindable({"username", "repository"})
    public boolean isFetchCommitsEnabled() {
        return !TextUtils.isEmpty(state.username) && !TextUtils.isEmpty(state.repository);
    }

    public void fetchCommits() {
        disposables.add(loadCommits(new LoadCommitsRequest(state.username, state.repository)));
    }

    private Disposable loadCommits(LoadCommitsRequest request) {
        return gitHubService.loadCommits(request)
                .flatMap(response -> Observable.fromIterable(response.getCommits()))
                .map(commit -> new CommitView(
                        commit.getCommitMessage(),
                        context.getString(R.string.author_format, commit.getAuthor())))
                .toList()
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
                .subscribe(this::setCommits, this::handleError);
    }

    private void handleError(Throwable throwable) {
        String message = throwable.getMessage();
        view.displayError(message);
    }

    public String getVersion() {
        return context.getString(R.string.version_format, BuildConfig.VERSION_NAME);
    }

    public String getFingerprint() {
        return context.getString(R.string.fingerprint_format, BuildConfig.VERSION_FINGERPRINT);
    }
}
