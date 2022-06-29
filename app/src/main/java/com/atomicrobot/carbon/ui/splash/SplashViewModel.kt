package com.atomicrobot.carbon.ui.splash

import android.app.Application
import android.os.Parcelable
import com.atomicrobot.carbon.ui.BaseViewModel
import android.net.Uri
import android.view.View
import com.atomicrobot.carbon.ui.deeplink.DeepLinkInteractor
import com.atomicrobot.carbon.ui.NavigationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val app: Application,
    private val deepLinkInteractor: DeepLinkInteractor
)
 : BaseViewModel(app) {

//    @Parcelize
//    class State : Parcelable

    sealed class ViewNavigation {
        object None : ViewNavigation()
        object FirstTime : ViewNavigation()
    }

    private val _navigationEvent = MutableStateFlow<ViewNavigation>(ViewNavigation.None)
    val navigationEvent: StateFlow<ViewNavigation>
    get() = _navigationEvent

    override fun setupViewModel() {
        _navigationEvent.update { ViewNavigation.FirstTime }
    }

    fun setDeepLinkUri(uri: Uri?) {
        deepLinkInteractor.setDeepLinkUri(uri)
    }

    fun setDeepLinkPath(path: String?) {
        deepLinkInteractor.setDeepLinkPath(path)
    }
    fun getDeepLinkNavDestination(): String {
        return deepLinkInteractor.getDeepLinkNavDestination()
    }

    fun getDeepLinkTextColor(): Int {
        return deepLinkInteractor.getDeepLinkTextColor()
    }

    fun getDeepLinkTextSize(): Float {
        return deepLinkInteractor.getDeepLinkTextSize()
    }


    companion object {
        private const val STATE_KEY = "SplashViewModelState"  // NON-NLS
    }
}
