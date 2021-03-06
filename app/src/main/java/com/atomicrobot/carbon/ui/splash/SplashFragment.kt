package com.atomicrobot.carbon.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.databinding.FragmentSplashBinding
import com.atomicrobot.carbon.ui.BaseFragment
import com.atomicrobot.carbon.ui.NavigationEvent

class SplashFragment : BaseFragment() {

    private lateinit var viewModel: SplashViewModel
    private lateinit var binding: FragmentSplashBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        viewModel = getViewModel(SplashViewModel::class)

        viewModel.navigationEvent.observe(
                this,
                object : NavigationEvent.NavigationObserver<SplashViewModel.ViewNavigation> {
                    override fun onNavigationEvent(event: SplashViewModel.ViewNavigation) {
                        when (event) {
                            SplashViewModel.ViewNavigation.FirstTime -> navigateFirstTime()
                        }.let { /* exhaustive */ }
                    }
                })

        binding = FragmentSplashBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
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

    private fun navigateFirstTime() {
        findNavController().navigate(R.id.action_splashFragment_to_mainFragment)
    }
}
