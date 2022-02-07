package com.atomicrobot.carbon.ui.devsettings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.ui.BaseFragment

class DevSettingsFragment : BaseFragment() {
    interface DevSettingsFragmentHost

    private val viewModel: DevSettingsViewModel by activityViewModels()
    private lateinit var binding: DevSettingsFragmentBinding
    private var host: DevSettingsFragmentHost? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        host = context as DevSettingsFragmentHost
    }

    override fun onDetach() {
        host = null
        super.onDetach()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dev_settings, container, false)
        binding.vm = viewModel

        return binding.root
    }

    override fun onDestroyView() {
        binding.unbind()
        super.onDestroyView()
    }
}
