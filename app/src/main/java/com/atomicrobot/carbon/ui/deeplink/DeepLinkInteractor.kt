package com.atomicrobot.carbon.ui.deeplink

import android.graphics.Color
import android.net.Uri
import com.atomicrobot.carbon.ComposeStartActivity
import timber.log.Timber
import java.lang.NumberFormatException
import javax.inject.Inject

class DeepLinkInteractor @Inject constructor(){
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
            when(path) {
                "/carbon-android" -> {
                    Timber.d("default deep link received")
                    return ComposeStartActivity.mainPage
                }
                "/carbon-android/path1" -> {
                    Timber.d("path1 deep link received")
                    return ComposeStartActivity.deepLinkPath1
                }
                else -> {
                    Timber.e("Deep link path not recognized")
                    return ComposeStartActivity.mainPage
                }
            }
        }
        return ComposeStartActivity.mainPage
    }

    fun getDeepLinkTextColor(): Int {
        var color = Color.BLACK
        deepLinkUri?.let { uri ->
            val textColor = uri.getQueryParameter("textColor")
            if(!textColor.isNullOrEmpty()) {
                try {
                    color = Color.parseColor(textColor)
                } catch(exception: IllegalArgumentException) {
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
            if(!textSize.isNullOrEmpty()) {
                try {
                    size = textSize.toFloat()
                } catch(exception: NumberFormatException) {
                    Timber.e("Unsupported value for size")
                }
            }
        }

        return size
    }
}