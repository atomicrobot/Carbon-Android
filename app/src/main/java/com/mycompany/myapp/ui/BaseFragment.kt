package com.mycompany.myapp.ui

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import kotlin.reflect.KClass

abstract class BaseFragment: Fragment() {
    fun <VM: ViewModel> getViewModel(viewModelClass: KClass<VM>): VM {
        return ViewModelProviders.of(activity!!).get(viewModelClass.java)
    }
}