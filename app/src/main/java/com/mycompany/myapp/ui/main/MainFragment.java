package com.mycompany.myapp.ui.main;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mycompany.myapp.CommitItemBinding;
import com.mycompany.myapp.R;
import com.mycompany.myapp.ui.BaseFragment;
import com.mycompany.myapp.ui.main.MainPresenter.CommitView;
import com.mycompany.myapp.util.recyclerview.ArrayAdapter;

import javax.inject.Inject;

public class MainFragment extends BaseFragment {
    public interface MainFragmentHost {
        void inject(MainFragment fragment);
    }

    @Inject MainPresenter presenter;
    private MainFragmentBinding binding;
    private MainFragmentHost host;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        host = (MainFragmentHost) context;
        host.inject(this);
    }

    @Override
    public void onDetach() {
        host = null;
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        binding.setPresenter(presenter);

        binding.commits.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.commits.setAdapter(new CommitsAdapter());

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        binding.unbind();
        super.onDestroyView();
    }

    private static class CommitsAdapter extends ArrayAdapter<CommitView, CommitViewHolder> {
        @Override
        public CommitViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            CommitItemBinding commitItemBinding = CommitItemBinding.inflate(layoutInflater, parent, false);
            return new CommitViewHolder(commitItemBinding);
        }

        @Override
        public void onBindViewHolder(CommitViewHolder holder, int position) {
            CommitView commit = getItemAtPosition(position);
            holder.bind(commit);
        }
    }

    static class CommitViewHolder extends RecyclerView.ViewHolder {
        private final CommitItemBinding binding;

        private CommitViewHolder(CommitItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(CommitView item) {
            binding.setItem(item);
            binding.executePendingBindings();
        }
    }
}
