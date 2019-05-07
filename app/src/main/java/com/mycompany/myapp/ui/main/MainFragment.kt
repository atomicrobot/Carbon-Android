package com.mycompany.myapp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.mycompany.myapp.CommitItemBinding
import com.mycompany.myapp.data.api.github.model.Commit
import com.mycompany.myapp.databinding.FragmentMainBinding
import com.mycompany.myapp.ui.BaseFragment
import com.mycompany.myapp.ui.SimpleSnackbarMessage
import com.mycompany.myapp.util.recyclerview.ArrayAdapter

class MainFragment : BaseFragment() {
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = getViewModel(MainViewModel::class)

        binding = FragmentMainBinding.inflate(inflater, container, false)
        binding.vm = viewModel

        viewModel.snackbarMessage.observe(this, object : SimpleSnackbarMessage.SnackbarObserver {
            override fun onNewMessage(message: String) {
                Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
            }
        })

        (activity as AppCompatActivity).let {
            it.setSupportActionBar(binding.toolbar)
            binding.toolbar.title = it.packageName
        }

        binding.commits.layoutManager = LinearLayoutManager(activity)
        binding.commits.adapter = CommitsAdapter()

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.saveState(outState)
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    override fun onDestroyView() {
        binding.unbind()
        super.onDestroyView()
    }

    private class CommitsAdapter : ArrayAdapter<Commit, CommitViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommitViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = CommitItemBinding.inflate(layoutInflater, parent, false)
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
