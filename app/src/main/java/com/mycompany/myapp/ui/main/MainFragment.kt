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
import com.mycompany.myapp.ui.BaseFragment
import com.mycompany.myapp.ui.main.MainPresenter.CommitView
import com.mycompany.myapp.util.recyclerview.ArrayAdapter

import javax.inject.Inject

class MainFragment : BaseFragment() {
    interface MainFragmentHost {
        fun inject(fragment: MainFragment)
    }

    @Inject lateinit var presenter: MainPresenter
    private lateinit var binding: MainFragmentBinding
    private var host: MainFragmentHost? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        host = context as MainFragmentHost
        host!!.inject(this)
    }

    override fun onDetach() {
        host = null
        super.onDetach()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater!!, R.layout.fragment_main, container, false)
        binding.presenter = presenter

        binding.commits.layoutManager = LinearLayoutManager(activity)
        binding.commits.adapter = CommitsAdapter()

        return binding.root
    }

    override fun onDestroyView() {
        binding.unbind()
        super.onDestroyView()
    }

    private class CommitsAdapter : ArrayAdapter<CommitView, CommitViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommitViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val commitItemBinding = CommitItemBinding.inflate(layoutInflater, parent, false)
            return CommitViewHolder(commitItemBinding)
        }

        override fun onBindViewHolder(holder: CommitViewHolder, position: Int) {
            val commit = getItemAtPosition(position)
            holder.bind(commit)
        }
    }

    internal class CommitViewHolder constructor(
            private val binding: CommitItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CommitView) {
            binding.item = item
            binding.executePendingBindings()
        }
    }
}
