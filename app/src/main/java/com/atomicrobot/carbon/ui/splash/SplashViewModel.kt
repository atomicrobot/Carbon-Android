package com.atomicrobot.carbon.ui.splash

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.atomicrobot.carbon.ui.deeplink.DeepLinkInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val deepLinkInteractor: DeepLinkInteractor
) : ViewModel() {

    sealed class ViewNavigation {
        object None : ViewNavigation()
        object FirstTime : ViewNavigation()
    }

    private val _navigationEvent = MutableStateFlow<ViewNavigation>(ViewNavigation.None)
    val navigationEvent: StateFlow<ViewNavigation>
        get() = _navigationEvent

    fun navigateFirstTime() {
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
}
