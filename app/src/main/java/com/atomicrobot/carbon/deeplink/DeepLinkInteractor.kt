package com.atomicrobot.carbon.deeplink

import com.atomicrobot.carbon.R
import timber.log.Timber

class DeepLinkInteractor {
    private var deepLinkPath: String? = null

    fun getDeepLinkPath(): String? {
        return this.deepLinkPath
    }

    fun setDeepLinkPath(path: String?) {
        this.deepLinkPath = path
    }

    fun getNavResourceFromDeepLink(): Int? {
        deepLinkPath?.let { path ->
            when(path) {
                "/carbon-android" -> {
                    Timber.d("default deep link received")
                    return null
                }
                "/carbon-android/path1" -> {
                    Timber.d("path1 deep link received")
                    return R.id.action_mainFragment_to_deepLinkPath1Fragment
                }
                "/carbon-android/path2" -> {
                    Timber.d("path2 deep link received")
                    return null
                }
                else -> {
                    Timber.e("Deep link path not recognized")
                    return null
                }
            }
        }
        return null
    }
}