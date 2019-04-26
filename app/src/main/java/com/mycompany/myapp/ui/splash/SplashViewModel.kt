package com.mycompany.myapp.ui.splash

import androidx.lifecycle.ViewModel
import com.mycompany.myapp.ui.NavigationEvent

class SplashViewModel() : ViewModel() {

    sealed class ViewNavigation {
        object FirstTime : ViewNavigation()
    }

    val navigationEvent = NavigationEvent<ViewNavigation>()

    fun onResume() {
        // TODO: Update these conditions
        navigationEvent.postValue(ViewNavigation.FirstTime)
    }
}
