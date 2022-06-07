package com.atomicrobot.carbon.ui.deeplink

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.lifecycle.HiltViewModel
import com.atomicrobot.carbon.databinding.FragmentDeepLinkSampleBinding


class DeepLinkSampleFragment : Fragment() {
    private val viewModel: DeepLinkSampleViewModel by viewModels()
    private lateinit var binding: FragmentDeepLinkSampleBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDeepLinkSampleBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        viewModel.onResume()
    }
}