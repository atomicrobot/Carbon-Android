package com.mycompany.myapp.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.mycompany.myapp.BuildConfig;
import com.mycompany.myapp.R;
import com.mycompany.myapp.data.api.github.GitHubBusService;
import com.mycompany.myapp.data.api.github.GitHubBusService.LoadCommitsRequest;
import com.mycompany.myapp.data.api.github.GitHubBusService.LoadCommitsResponse;
import com.mycompany.myapp.data.api.github.model.Commit;
import com.mycompany.myapp.ui.BaseFragment;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import hugo.weaving.DebugLog;
import icepick.Icepick;
import icepick.Icicle;

public class MainFragment extends BaseFragment<MainComponent> {

    @Inject
    GitHubBusService gitHubBusService;

    @InjectView(R.id.username)
    EditText userNameView;

    @InjectView(R.id.repository)
    EditText repositoryView;

    @InjectView(R.id.commits)
    RecyclerView commitsView;

    @InjectView(R.id.version)
    TextView versionView;

    @InjectView(R.id.fingerprint)
    TextView fingerprintView;

    @Icicle
    String username = "madebyatomicrobot";

    @Icicle
    String repository = "android-starter-project";

    private CommitsAdapter adapter;

    @Override
    protected void inject(MainComponent component) {
        component.inject(this);
    }

    @DebugLog
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.inject(this, view);
        Icepick.restoreInstanceState(this, savedInstanceState);

        commitsView.setHasFixedSize(true);
        commitsView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @DebugLog
    @Override
    public void onDestroyView() {
        ButterKnife.reset(this);
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new CommitsAdapter();
        commitsView.setAdapter(adapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @DebugLog
    @Override
    public void onResume() {
        super.onResume();

        userNameView.setText(username);
        repositoryView.setText(repository);

        versionView.setText(String.format("Version: %s", BuildConfig.VERSION_NAME));
        fingerprintView.setText(String.format("Fingerprint: %s", BuildConfig.VERSION_FINGERPRINT));
    }

    @OnClick(R.id.fetch_commits)
    public void handleFetchCommits() {
        username = userNameView.getText().toString();
        repository = repositoryView.getText().toString();

        gitHubBusService.loadCommits(new LoadCommitsRequest(username, repository));
    }

    @Subscribe
    public void handleLoadCommitsResponse(LoadCommitsResponse response) {
        adapter.commits = response.getCommits();
        adapter.notifyDataSetChanged();
    }

    public class CommitsAdapter extends RecyclerView.Adapter<ViewHolder> {
        private List<Commit> commits = new ArrayList<>();

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_commit_summary, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Commit commit = commits.get(position);
            holder.messageView.setText(commit.getCommitMessage());
            holder.authorView.setText(commit.getAuthor());
        }

        @Override
        public int getItemCount() {
            return commits.size();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.message)
        TextView messageView;

        @InjectView(R.id.author)
        TextView authorView;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }
}
