package com.mycompany.myapp.ui.main

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import com.mycompany.myapp.R
import com.mycompany.myapp.ui.BaseActivity
import com.mycompany.myapp.ui.main.MainComponent.MainModule
import com.mycompany.myapp.ui.main.MainFragment.MainFragmentHost
import com.mycompany.myapp.ui.main.MainPresenter.MainViewContract
import javax.inject.Inject

class MainActivity : BaseActivity(), MainViewContract, MainFragmentHost {
    private lateinit var component: MainComponent
    @Inject lateinit var presenter: MainPresenter
    private lateinit var binding: MainActivityBinding

    public override fun onCreate(savedInstanceState: Bundle?) {
        component = appComponent.mainComponent(MainModule(this))
        component.inject(this)

        presenter.view = this
        lifecycle.addObserver(presenter)
        presenter.restoreState(savedInstanceState)

        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.presenter = presenter
        binding.executePendingBindings()

        setSupportActionBar(binding.toolbar)
        supportActionBar!!.title = applicationContext.packageName
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        presenter.saveState(outState)
    }

    override fun inject(fragment: MainFragment) = component.inject(fragment)

    override fun displayError(message: String) {
        Snackbar.make(binding.rootLayout, message, Snackbar.LENGTH_LONG).show()
    }
}