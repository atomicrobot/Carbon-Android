package com.atomicrobot.carbon.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.app.ApplicationComponent
import com.atomicrobot.carbon.app.MainApplication
import javax.inject.Inject
import kotlin.reflect.KClass

abstract class BaseActivity : AppCompatActivity() {
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    protected val appComponent: ApplicationComponent
        get() = (application as MainApplication).component

    fun <VM: ViewModel> getViewModel(viewModelClass: KClass<VM>): VM {
        return ViewModelProviders.of(this, viewModelFactory).get(viewModelClass.java)
    }

    override fun onSupportNavigateUp() =
            Navigation.findNavController(findViewById(R.id.nav_host_fragment)).navigateUp()
}
