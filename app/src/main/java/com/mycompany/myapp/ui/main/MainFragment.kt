package com.mycompany.myapp.ui.main

import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mycompany.myapp.CommitItemBinding
import com.mycompany.myapp.R
import com.mycompany.myapp.data.api.github.model.Commit
import com.mycompany.myapp.ui.BaseFragment
import com.mycompany.myapp.util.recyclerview.ArrayAdapter

class MainFragment : BaseFragment() {
    interface MainFragmentHost

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: MainFragmentBinding
    private var host: MainFragmentHost? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        host = context as MainFragmentHost
    }

    override fun onDetach() {
        host = null
        super.onDetach()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = getViewModel(MainViewModel::class)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        binding.vm = viewModel

        binding.commits.layoutManager = LinearLayoutManager(activity)
        binding.commits.adapter = CommitsAdapter()

        return binding.root
    }

    override fun onDestroyView() {
        binding.unbind()
        super.onDestroyView()
    }

    private class CommitsAdapter : ArrayAdapter<Commit, CommitViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommitViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding: CommitItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_commit_summary, parent, false)
            return CommitViewHolder(binding)
        }

        override fun onBindViewHolder(holder: CommitViewHolder, position: Int) {
            val commit = getItemAtPosition(position)
            holder.bind(commit)
        }
    }

    private class CommitViewHolder(
            private val binding: CommitItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Commit) {
            binding.item = item
            binding.executePendingBindings()
        }
    }
}
