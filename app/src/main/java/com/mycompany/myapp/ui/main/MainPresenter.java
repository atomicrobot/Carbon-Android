package com.mycompany.myapp.ui.main;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

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
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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
        public CommitView(String message, String author) {
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
    public static class State extends BaseObservable {
        boolean initialized = false;

        String username = "madebyatomicrobot";
        String repository = "android-starter-project";
        List<CommitView> commits = new ArrayList<>();

        @Bindable
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
            notifyPropertyChanged(BR.username);
        }

        @Bindable
        public String getRepository() {
            return repository;
        }

        public void setRepository(String repository) {
            this.repository = repository;
            notifyPropertyChanged(BR.repository);
        }

        @Bindable
        public List<CommitView> getCommits() {
            return commits;
        }

        public void setCommits(List<CommitView> commits) {
            this.commits = commits;
            notifyPropertyChanged(BR.commits);
        }
    }

    private final Context context;
    private final GitHubService gitHubService;

    public MainPresenter(Context context, GitHubService gitHubService) {
        super(STATE_KEY);
        this.context = context;
        this.gitHubService = gitHubService;

        this.state = new State();
    }

    public void onResume() {
        super.onResume();
        if (!state.initialized) {
            state.initialized = true;
            fetchCommits();
        }
    }

    public void fetchCommits() {
        disposables.add(loadCommits(new LoadCommitsRequest(
                state.getUsername(),
                state.getRepository())));
    }

    private Disposable loadCommits(LoadCommitsRequest request) {
        return gitHubService.loadCommits(request)
                .flatMap(response -> Observable.fromIterable(response.getCommits()))
                .map(commit -> new CommitView(
                        commit.getCommitMessage(),
                        context.getString(R.string.author_format, commit.getAuthor())))
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleLoadCommitsResponse, this::handleError);
    }

    private void handleLoadCommitsResponse(List<CommitView> commits) {
        state.setCommits(commits);
    }

    private void handleError(Throwable throwable) {
        String message = throwable.getMessage();
        view.displayError(message);
    }

    public String getVersion() {
        return String.format("Version: %s", BuildConfig.VERSION_NAME);
    }

    public String getFingerprint() {
        return String.format("Fingerprint: %s", BuildConfig.VERSION_FINGERPRINT);
    }
}
