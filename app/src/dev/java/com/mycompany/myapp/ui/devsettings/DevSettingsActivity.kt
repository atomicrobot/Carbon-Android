package com.mycompany.myapp.ui.devsettings

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import com.mycompany.myapp.R
import com.mycompany.myapp.ui.BaseActivity
import com.mycompany.myapp.ui.devsettings.DevSettingsComponent.DevSettingsModule
import com.mycompany.myapp.ui.devsettings.DevSettingsFragment.DevSettingsFragmentHost
import com.mycompany.myapp.ui.devsettings.DevSettingsPresenter.DevSettingsViewContract
import javax.inject.Inject

class DevSettingsActivity : BaseActivity(), DevSettingsViewContract, DevSettingsFragmentHost {

    private lateinit var component: DevSettingsComponent
    @Inject lateinit var presenter: DevSettingsPresenter
    private lateinit var binding: DevSettingsActivityBinding

    public override fun onCreate(savedInstanceState: Bundle?) {
        component = appComponent.devSettingsComponent(DevSettingsModule(this))
        component.inject(this)

        presenter.view = this
        lifecycle.addObserver(presenter)
        presenter.restoreState(savedInstanceState)

        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_dev_settings)
        binding.presenter = presenter
        binding.executePendingBindings()

        setSupportActionBar(binding.toolbar)
        supportActionBar!!.title = "Dev Settings"
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        presenter.saveState(outState)
    }

    override fun inject(fragment: DevSettingsFragment) = component.inject(fragment)

    companion object {
        fun buildIntent(context: Context): Intent {
            return Intent(context, DevSettingsActivity::class.java)
        }
    }
}
