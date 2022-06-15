package com.atomicrobot.carbon.ui.deeplink

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.atomicrobot.carbon.databinding.FragmentDeepLinkSampleBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class DeepLinkSampleFragment : Fragment() {
    private val viewModel: DeepLinkSampleViewModel by viewModel()
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