package com.mycompany.myapp

import android.os.Bundle
import com.mycompany.myapp.ui.BaseActivity
import com.mycompany.myapp.ui.main.MainViewModel

class StartActivity : BaseActivity() {
    private lateinit var viewModel: MainViewModel

    public override fun onCreate(savedInstanceState: Bundle?) {
        appComponent.inject(this)
        super.onCreate(savedInstanceState)

        viewModel = getViewModel(MainViewModel::class)

        setContentView(R.layout.activity_start)
    }
}