package com.atomicrobot.carbon.ui.splash

import android.app.Application
import android.net.Uri
import android.os.Parcelable
import com.atomicrobot.carbon.deeplink.DeepLinkInteractor
import com.atomicrobot.carbon.ui.BaseViewModel
import com.atomicrobot.carbon.ui.NavigationEvent
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

class SplashViewModel @Inject constructor(
        private val app: Application,
        private val deepLinkInteractor: DeepLinkInteractor)
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

    fun setDeepLinkUri(uri: Uri?) {
        deepLinkInteractor.setDeepLinkUri(uri)
    }

    fun setDeepLinkPath(path: String?) {
        deepLinkInteractor.setDeepLinkPath(path)
    }

    companion object {
        private const val STATE_KEY = "SplashViewModelState"  // NON-NLS
    }
}
