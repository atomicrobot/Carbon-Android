package com.atomicrobot.carbon

import android.os.Bundle
import com.atomicrobot.carbon.ui.BaseActivity
import com.atomicrobot.carbon.ui.main.MainViewModel
import com.atomicrobot.carbon.R

class StartActivity : BaseActivity() {
    private lateinit var viewModel: MainViewModel

    public override fun onCreate(savedInstanceState: Bundle?) {
        appComponent.inject(this)
        super.onCreate(savedInstanceState)

        viewModel = getViewModel(MainViewModel::class)

        setContentView(R.layout.activity_start)
    }
}