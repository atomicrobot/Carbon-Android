package com.mycompany.myapp.ui.main;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mycompany.myapp.R;
import com.mycompany.myapp.ui.main.MainPresenter.CommitViewModel;
import com.mycompany.myapp.ui.main.MainPresenter.MainViewContract;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import pocketknife.PocketKnife;

public class MainFragment extends Fragment implements MainViewContract {
    public interface MainFragmentHost {
        void inject(MainFragment fragment);
    }

    @Inject MainPresenter presenter;

    @Bind(R.id.username) EditText userNameView;

    @Bind(R.id.repository) EditText repositoryView;

    @Bind(R.id.commits) RecyclerView commitsView;

    @Bind(R.id.version) TextView versionView;

    @Bind(R.id.fingerprint) TextView fingerprintView;

    private MainFragmentHost host;

    private CommitsAdapter adapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        host = (MainFragmentHost) activity;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        host.inject(this);

        presenter.setView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);

        commitsView.setHasFixedSize(true);
        commitsView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        PocketKnife.restoreInstanceState(this, savedInstanceState); // FIXME

        adapter = new CommitsAdapter();
        commitsView.setAdapter(adapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        PocketKnife.saveInstanceState(this, outState); // FIXME
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void onPause() {
        presenter.onPause();
        super.onPause();
    }

    @Override
    public void displayUsername(String username) {
        userNameView.setText(username);
    }

    @Override
    public void displayRepository(String repository) {
        repositoryView.setText(repository);
    }

    @Override
    public void displayCommits(List<CommitViewModel> commits) {
        adapter.setCommits(commits);
    }

    @Override
    public void displayError(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void displayVersion(String version) {
        versionView.setText(version);
    }

    @Override
    public void displayFingerprint(String fingerprint) {
        fingerprintView.setText(fingerprint);
    }

    @OnTextChanged(R.id.username)
    void handleUsernameChanged(CharSequence username) {
        presenter.setUsername(username.toString());
    }

    @OnTextChanged(R.id.repository)
    void handleRepositoryChanged(CharSequence repository) {
        presenter.setRepository(repository.toString());
    }

    @OnClick(R.id.fetch_commits)
    void handleFetchCommits() {
        presenter.fetchCommits();
    }

    private static class CommitsAdapter extends RecyclerView.Adapter<ViewHolder> {
        private List<CommitViewModel> commits = new ArrayList<>();

        private void setCommits(List<CommitViewModel> commits) {
            this.commits = commits;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_commit_summary, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            CommitViewModel commit = commits.get(position);
            holder.messageView.setText(commit.getMessage());
            holder.authorView.setText(commit.getAuthor());
        }

        @Override
        public int getItemCount() {
            return commits.size();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.message) TextView messageView;

        @Bind(R.id.author) TextView authorView;

        private ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
