package com.atomicrobot.carbon.ui

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.KClass
abstract class BaseFragment: Fragment() {
    fun <VM: ViewModel> getViewModel(viewModelClass: KClass<VM>): VM {
        return ViewModelProvider(requireActivity()).get(viewModelClass.java)
    }
}

//TODO all that's needed for compose, uncomment once compose is finished
//abstract class BaseFragment: Fragment()