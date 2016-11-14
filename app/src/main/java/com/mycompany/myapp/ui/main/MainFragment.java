package com.mycompany.myapp.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.mycompany.myapp.R;
import com.mycompany.myapp.ui.main.MainPresenter.CommitViewModel;
import com.mycompany.myapp.ui.main.MainPresenter.MainViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Unbinder;

public class MainFragment extends Fragment {
    public interface MainFragmentHost {
        void inject(MainFragment fragment);

        void setUsername(String username);

        void setRepository(String repository);

        void fetchCommits();
    }

    @BindView(R.id.username) EditText userNameView;
    @BindView(R.id.repository) EditText repositoryView;
    @BindView(R.id.commits) RecyclerView commitsView;
    @BindView(R.id.version) TextView versionView;
    @BindView(R.id.fingerprint) TextView fingerprintView;

    private MainFragmentHost host;
    private Unbinder unbinder;

    private CommitsAdapter adapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        host = (MainFragmentHost) context;
    }

    @Override
    public void onDetach() {
        host = null;
        super.onDetach();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        host.inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        unbinder = ButterKnife.bind(this, view);

        commitsView.setHasFixedSize(true);
        commitsView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new CommitsAdapter();
        commitsView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    public void render(MainViewModel viewModel) {
        userNameView.setText(viewModel.username());
        repositoryView.setText(viewModel.repository());
        fingerprintView.setText(viewModel.fingerprint());
        versionView.setText(viewModel.version());
        adapter.setCommits(viewModel.commits());
    }


    @OnTextChanged(R.id.username)
    void handleUsernameChanged(CharSequence username) {
        host.setUsername(username.toString());
    }

    @OnTextChanged(R.id.repository)
    void handleRepositoryChanged(CharSequence repository) {
        host.setRepository(repository.toString());
    }

    @OnClick(R.id.fetch_commits)
    void handleFetchCommits() {
        host.fetchCommits();
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
        @BindView(R.id.message) TextView messageView;
        @BindView(R.id.author) TextView authorView;

        private ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
