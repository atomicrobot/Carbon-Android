package com.atomicrobot.carbon

import android.os.Bundle
import com.atomicrobot.carbon.ui.BaseActivity

class StartActivity : BaseActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_start)
    }
}