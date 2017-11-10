package com.mycompany.myapp.ui

import android.support.v7.app.AppCompatActivity
import com.mycompany.myapp.app.ApplicationComponent
import com.mycompany.myapp.app.MainApplication

abstract class BaseActivity : AppCompatActivity() {
    protected val appComponent: ApplicationComponent
        get() = (application as MainApplication).component
}
