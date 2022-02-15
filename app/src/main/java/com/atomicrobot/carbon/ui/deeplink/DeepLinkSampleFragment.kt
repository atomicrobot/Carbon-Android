package com.atomicrobot.carbon.ui.deeplink

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.atomicrobot.carbon.databinding.FragmentDeepLinkSampleBinding

class DeepLinkSampleFragment : Fragment() {
    private lateinit var binding: FragmentDeepLinkSampleBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDeepLinkSampleBinding.inflate(inflater, container, false)

        return binding.root
    }
}