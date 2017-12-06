package com.mycompany.myapp.ui

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import com.mycompany.myapp.app.ApplicationComponent
import com.mycompany.myapp.app.MainApplication
import javax.inject.Inject
import kotlin.reflect.KClass

abstract class BaseActivity : AppCompatActivity() {
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    protected val appComponent: ApplicationComponent
        get() = (application as MainApplication).component

    fun <VM: ViewModel> getViewModel(viewModelClass: KClass<VM>): VM {
        return ViewModelProviders.of(this, viewModelFactory).get(viewModelClass.java)
    }
}
