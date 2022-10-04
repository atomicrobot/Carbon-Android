package com.atomicrobot.carbon.ui.splash

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.atomicrobot.carbon.deeplink.DeepLinkInteractor
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val deepLinkInteractor: DeepLinkInteractor
) : ViewModel() {

    fun setDeepLinkUri(uri: Uri?) {
        deepLinkInteractor.setDeepLinkUri(uri)
    }

    fun setDeepLinkPath(path: String?) {
        deepLinkInteractor.setDeepLinkPath(path)
    }
}
