package com.mycompany.myapp.ui.main;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.mycompany.myapp.R;
import com.mycompany.myapp.data.api.github.model.Commit;
import com.mycompany.myapp.databinding.SampleBinding;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Unbinder;

public class SampleFragment extends LifecycleFragment {
    public interface SampleFragmentHost {
        void inject(SampleFragment fragment);
    }

    @BindView(R.id.username) EditText userNameView;
    @BindView(R.id.repository) EditText repositoryView;
    @BindView(R.id.commits) RecyclerView commitsView;
    @BindView(R.id.version) TextView versionView;
    @BindView(R.id.fingerprint) TextView fingerprintView;

    private SampleFragmentHost host;
    private Unbinder unbinder;

    private CommitsAdapter adapter;
    private SampleViewModel viewModel;
    private SampleFragmentState state = new SampleFragmentState();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        host = (SampleFragmentHost) context;
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
        viewModel = ViewModelProviders.of(this).get(SampleViewModel.class);

        viewModel.getViewState().observe(this, this::render);
        viewModel.fetchCommits();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SampleBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sample, container, false);
        binding.setState(state);
        View view = binding.getRoot();
        unbinder = ButterKnife.bind(this, view);

        commitsView.setHasFixedSize(true);
        commitsView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new CommitsAdapter();
        commitsView.setAdapter(adapter);

        return view;
    }

    public void render(SampleFragmentState viewState) {
        this.state = viewState;
        ((SampleBinding) DataBindingUtil.getBinding(getView())).setState(state);
        if (this.state != null) {
            displayCommits(state.getCommits());
        }
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    public void displayCommits(List<Commit> commits) {
        adapter.setCommits(commits);
    }

    @OnTextChanged(R.id.username)
    void handleUsernameChanged(CharSequence username) {
        viewModel.setUsername(username.toString());
    }

    @OnTextChanged(R.id.repository)
    void handleRepositoryChanged(CharSequence repository) {
        viewModel.setRepository(repository.toString());
    }

    @OnClick(R.id.fetch_commits)
    void handleFetchCommits() {
        viewModel.fetchCommits();
    }

    private static class CommitsAdapter extends RecyclerView.Adapter<ViewHolder> {
        private List<Commit> commits = new ArrayList<>();

        private void setCommits(List<Commit> commits) {
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
            Commit commit = commits.get(position);
            holder.messageView.setText(commit.getCommitMessage());
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
