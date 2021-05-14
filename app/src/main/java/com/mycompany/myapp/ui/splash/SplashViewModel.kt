package com.mycompany.myapp.ui.splash

import android.app.Application
import android.os.Parcelable
import com.mycompany.myapp.ui.BaseViewModel
import com.mycompany.myapp.ui.NavigationEvent
import kotlinx.android.parcel.Parcelize
import javax.inject.Inject

class SplashViewModel @Inject constructor(
        private val app: Application)
    : BaseViewModel<SplashViewModel.State>(app, STATE_KEY, State()) {

    @Parcelize
    class State : Parcelable

    sealed class ViewNavigation {
        object FirstTime : ViewNavigation()
    }

    val navigationEvent = NavigationEvent<ViewNavigation>()

    override fun setupViewModel() {
        navigationEvent.postValue(ViewNavigation.FirstTime)
    }

    companion object {
        private const val STATE_KEY = "SplashViewModelState"  // NON-NLS
    }
}
