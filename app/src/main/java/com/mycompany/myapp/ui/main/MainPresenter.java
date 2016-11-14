package com.mycompany.myapp.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mycompany.myapp.BuildConfig;
import com.mycompany.myapp.data.api.github.GitHubService;
import com.mycompany.myapp.data.api.github.GitHubService.LoadCommitsRequest;
import com.mycompany.myapp.data.api.github.GitHubService.LoadCommitsResponse;
import com.mycompany.myapp.data.api.github.model.Commit;
import com.mycompany.myapp.util.RxUtils;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;
import org.parceler.Parcels;

import java.util.Collections;
import java.util.List;

import auto.parcelgson.AutoParcelGson;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;
import rx.subscriptions.CompositeSubscription;

public class MainPresenter {
    private static final String EXTRA_STATE = "MainPresenterState";

    public interface MainViewContract {
        void displayError(String message);

        void render(MainViewModel viewModel);
    }

    private static CommitViewModel mapCommitToViewModel(Commit commit) {
        String message = commit.getCommitMessage();
        String author = commit.getAuthor();
        return new CommitViewModel(message, author);
    }

    private static Observable<MainViewModel> toViewModel(State state) {
        return Observable.from(state.commits())
                .map(MainPresenter::mapCommitToViewModel)
                .toList()
                .map(commitViewModels -> MainViewModel.builder()
                        .username(state.username())
                        .repository(state.repository())
                        .commits(commitViewModels)
                        .build());
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

    @AutoParcelGson
    public abstract static class MainViewModel {
        static Builder builder() {
            return new AutoParcelGson_MainPresenter_MainViewModel.Builder()
                    .username("")
                    .repository("")
                    .fingerprint(String.format("Fingerprint: %s", BuildConfig.VERSION_FINGERPRINT))
                    .version(String.format("Version: %s", BuildConfig.VERSION_NAME))
                    .commits(Collections.emptyList());

        }

        abstract String username();

        abstract String repository();

        abstract String fingerprint();

        abstract String version();

        abstract List<CommitViewModel> commits();

        abstract Builder toBuilder();

        @AutoParcelGson.Builder
        abstract static class Builder {
            abstract Builder username(String username);

            abstract Builder repository(String repository);

            abstract Builder fingerprint(String fingerprint);

            abstract Builder version(String version);

            abstract Builder commits(List<CommitViewModel> commits);

            abstract MainViewModel build();
        }
    }

    @AutoParcelGson
    public abstract static class State implements Parcelable {
        static Builder builder() {
            return new AutoParcelGson_MainPresenter_State.Builder()
                    .username("madebyatomicrobot")
                    .repository("android-starter-project")
                    .commits(Collections.emptyList());
        }

        abstract String username();

        abstract String repository();

        @Nullable
        abstract List<Commit> commits();

        abstract Builder toBuilder();

        @AutoParcelGson.Builder
        abstract static class Builder {
            abstract Builder username(String username);

            abstract Builder repository(String repository);

            abstract Builder commits(List<Commit> commits);

            abstract State build();
        }
    }

    private final Context context;
    private final GitHubService gitHubService;

    private CompositeSubscription subscriptions;
    private MainViewContract view;
    private State state;
    private BehaviorSubject<State> stateSubject;

    public MainPresenter(Context context, GitHubService gitHubService) {
        this.context = context;
        this.gitHubService = gitHubService;
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
        subscriptions = RxUtils.getNewCompositeSubIfUnsubscribed(subscriptions);
        if (state == null) {
            state = State.builder().build();
        }

        stateSubject = BehaviorSubject.create(state);
        subscriptions.add(stateSubject.distinctUntilChanged()
                .flatMap(MainPresenter::toViewModel)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(view::render, this::handleError));

        fetchCommits();
    }

    public void onPause() {
        RxUtils.unsubscribeIfNotNull(subscriptions);
    }

    public void setUsername(String username) {
        state = state.toBuilder().username(username).build();
    }

    public void setRepository(String repository) {
        state = state.toBuilder().repository(repository).build();
    }

    public void fetchCommits() {
        subscriptions.add(loadCommits(buildLoadCommitsRequest()));
    }

    private LoadCommitsRequest buildLoadCommitsRequest() {
        return new LoadCommitsRequest(state.username(), state.repository());
    }

    private Subscription loadCommits(LoadCommitsRequest request) {
        return gitHubService.loadCommits(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleLoadCommitsResponse, this::handleError);
    }

    private void handleLoadCommitsResponse(LoadCommitsResponse response) {
        state = state.toBuilder()
                .commits(response.getCommits())
                .build();
        stateSubject.onNext(state);
    }

    private void handleError(Throwable throwable) {
        String message = throwable.getMessage();
        view.displayError(message);
    }
}
