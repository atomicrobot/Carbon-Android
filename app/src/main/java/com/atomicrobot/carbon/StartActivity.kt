package com.atomicrobot.carbon

import android.os.Bundle
import com.atomicrobot.carbon.ui.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartActivity : BaseActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_start)
    }
}