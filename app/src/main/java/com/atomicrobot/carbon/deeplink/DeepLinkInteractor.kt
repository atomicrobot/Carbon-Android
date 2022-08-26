package com.atomicrobot.carbon.deeplink

import android.graphics.Color
import android.net.Uri
import com.atomicrobot.carbon.StartActivity
import com.atomicrobot.carbon.navigation.AppScreens
import timber.log.Timber
import java.lang.NumberFormatException

class DeepLinkInteractor {
    private var deepLinkUri: Uri? = null
    private var deepLinkPath: String? = null

    fun setDeepLinkUri(uri: Uri?) {
        this.deepLinkUri = uri
    }

    fun setDeepLinkPath(path: String?) {
        this.deepLinkPath = path
    }

    fun getDeepLinkNavDestination(): String {
        deepLinkPath?.let { path ->
            when (path) {
                "/carbon-android" -> {
                    Timber.d("default deep link received")
                    return AppScreens.Home.route
                }
                "/carbon-android/path1" -> {
                    Timber.d("path1 deep link received")
                    return StartActivity.deepLinkPath1
                }
                else -> {
                    Timber.e("Deep link path not recognized")
                    return AppScreens.Home.route
                }
            }
        }
        return StartActivity.mainPage
    }

    fun getDeepLinkTextColor(): Int {
        var color = Color.BLACK
        deepLinkUri?.let { uri ->
            val textColor = uri.getQueryParameter("textColor")
            if (!textColor.isNullOrEmpty()) {
                try {
                    color = Color.parseColor(textColor)
                } catch (exception: IllegalArgumentException) {
                    Timber.e("Unsupported value for color")
                }
            }
        }

        return color
    }

    fun getDeepLinkTextSize(): Float {
        var size = 30f
        deepLinkUri?.let { uri ->
            val textSize = uri.getQueryParameter("textSize")
            if (!textSize.isNullOrEmpty()) {
                try {
                    size = textSize.toFloat()
                } catch (exception: NumberFormatException) {
                    Timber.e("Unsupported value for size")
                }
            }
        }

        return size
    }
}
