package com.mycompany.myapp.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mycompany.myapp.BuildConfig;
import com.mycompany.myapp.R;
import com.mycompany.myapp.data.api.github.GitHubService;
import com.mycompany.myapp.data.api.github.GitHubService.LoadCommitsRequest;
import com.mycompany.myapp.data.api.github.GitHubService.LoadCommitsResponse;
import com.mycompany.myapp.data.api.github.model.Commit;
import com.mycompany.myapp.ui.BaseFragment;
import com.mycompany.myapp.util.RxUtils;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import hugo.weaving.DebugLog;
import pocketknife.PocketKnife;
import pocketknife.SaveState;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class MainFragment extends BaseFragment<MainComponent> {

    public interface MainFragmentListener {

    }

    @Inject
    MainFragmentListener listener;

    @Inject
    GitHubService gitHubService;

    @Bind(R.id.username)
    EditText userNameView;

    @Bind(R.id.repository)
    EditText repositoryView;

    @Bind(R.id.commits)
    RecyclerView commitsView;

    @Bind(R.id.version)
    TextView versionView;

    @Bind(R.id.fingerprint)
    TextView fingerprintView;

    @SaveState
    String username = "madebyatomicrobot";

    @SaveState
    String repository = "android-starter-project";

    private CompositeSubscription subscriptions;

    private CommitsAdapter adapter;

    @Override
    protected void inject(MainComponent component) {
        component.inject(this);
    }

    @DebugLog
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);

        commitsView.setHasFixedSize(true);
        commitsView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @DebugLog
    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        PocketKnife.restoreInstanceState(this, savedInstanceState);

        adapter = new CommitsAdapter();
        adapter.restore(savedInstanceState);
        commitsView.setAdapter(adapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        PocketKnife.saveInstanceState(this, outState);

        if (adapter != null) {
            adapter.save(outState);
        }
    }

    @DebugLog
    @Override
    public void onResume() {
        super.onResume();
        subscriptions = RxUtils.getNewCompositeSubIfUnsubscribed(subscriptions);

        userNameView.setText(username);
        repositoryView.setText(repository);

        versionView.setText(String.format("Version: %s", BuildConfig.VERSION_NAME));
        fingerprintView.setText(String.format("Fingerprint: %s", BuildConfig.VERSION_FINGERPRINT));
    }

    @Override
    public void onPause() {
        RxUtils.unsubscribeIfNotNull(subscriptions);
        super.onPause();
    }

    @OnTextChanged(R.id.username)
    public void handleUsernameChanged(CharSequence username) {
        this.username = username.toString();
    }

    @OnTextChanged(R.id.repository)
    public void handleRepositoryChanged(CharSequence repository) {
        this.repository = repository.toString();
    }

    @OnClick(R.id.fetch_commits)
    public void handleFetchCommits() {
        subscriptions.add(
                gitHubService.loadCommits(new LoadCommitsRequest(username, repository))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::handleLoadCommitsResponse, this::handleError));
    }

    private void handleError(Throwable throwable) {
        Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_LONG).show();
    }

    private void handleLoadCommitsResponse(LoadCommitsResponse response) {
        adapter.commits = response.getCommits();
        adapter.notifyDataSetChanged();
    }

    public class CommitsAdapter extends RecyclerView.Adapter<ViewHolder> {
        private static final String EXTRA_COMMITS = "commits";

        private List<Commit> commits = new ArrayList<>();

        public void save(Bundle bundle) {
            if (bundle != null) {
                bundle.putParcelable(EXTRA_COMMITS, Parcels.wrap(commits));
            }
        }

        public void restore(Bundle bundle) {
            if (bundle != null) {
                commits = Parcels.unwrap(bundle.getParcelable(EXTRA_COMMITS));
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_commit_summary, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Commit commit = commits.get(position);
            holder.messageView.setText(commit.getCommitMessage());
            holder.authorView.setText(formatAuthor(R.string.author_format, commit.getAuthor()));
        }

        @Override
        public int getItemCount() {
            return commits.size();
        }
    }

    private String formatAuthor(@StringRes int authorFormatId, String author) {
        return getString(authorFormatId, author);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.message)
        TextView messageView;

        @Bind(R.id.author)
        TextView authorView;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
