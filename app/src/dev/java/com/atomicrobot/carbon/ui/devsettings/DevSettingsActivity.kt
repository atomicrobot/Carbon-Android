package com.atomicrobot.carbon.ui.devsettings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.hilt.navigation.compose.hiltViewModel
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.ui.BaseActivity
import com.atomicrobot.carbon.ui.devsettings.DevSettingsFragment.DevSettingsFragmentHost

class DevSettingsActivity : BaseActivity(), DevSettingsFragmentHost {
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
