package com.mycompany.myapp.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.mycompany.myapp.R
import com.mycompany.myapp.databinding.FragmentSplashBinding
import com.mycompany.myapp.ui.BaseFragment
import com.mycompany.myapp.ui.NavigationEvent

class SplashFragment : BaseFragment() {

    private lateinit var viewModel: SplashViewModel

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

        val binding = FragmentSplashBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    private fun navigateFirstTime() {
        findNavController().navigate(R.id.action_splashFragment_to_mainFragment)
    }
}
