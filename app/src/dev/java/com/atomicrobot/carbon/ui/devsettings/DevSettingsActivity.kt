package com.atomicrobot.carbon.ui.devsettings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.ui.BaseActivity
import com.atomicrobot.carbon.ui.devsettings.DevSettingsFragment.DevSettingsFragmentHost
import org.koin.androidx.viewmodel.ext.android.viewModel

class DevSettingsActivity : BaseActivity(), DevSettingsFragmentHost {
    private val viewModel: DevSettingsViewModel by viewModel()
    private lateinit var binding: DevSettingsActivityBinding

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = DataBindingUtil.setContentView(this, R.layout.activity_dev_settings)
        binding.executePendingBindings()

        setSupportActionBar(binding.toolbar)
        supportActionBar!!.title = "Dev Settings"
    }

    companion object {
        fun buildIntent(context: Context): Intent {
            return Intent(context, DevSettingsActivity::class.java)
        }
    }
}
