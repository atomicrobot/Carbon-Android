package com.atomicrobot.carbon.ui.devsettings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.ui.BaseActivity
import com.atomicrobot.carbon.ui.devsettings.DevSettingsFragment.DevSettingsFragmentHost
import com.atomicrobot.carbon.ui.devsettings.DevSettingsActivityBinding
import javax.inject.Inject

class DevSettingsActivity : BaseActivity(), DevSettingsFragmentHost {
    @Inject lateinit var viewModel: DevSettingsViewModel
    private lateinit var binding: DevSettingsActivityBinding

    public override fun onCreate(savedInstanceState: Bundle?) {
        appComponent.inject(this)
        super.onCreate(savedInstanceState)

        viewModel = getViewModel(DevSettingsViewModel::class)
        viewModel.restoreState(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_dev_settings)
        binding.vm = viewModel
        binding.executePendingBindings()

        setSupportActionBar(binding.toolbar)
        supportActionBar!!.title = "Dev Settings"
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.saveState(outState)
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    companion object {
        fun buildIntent(context: Context): Intent {
            return Intent(context, DevSettingsActivity::class.java)
        }
    }
}
