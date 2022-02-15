package com.atomicrobot.carbon.deeplink

import com.atomicrobot.carbon.R
import timber.log.Timber

class DeepLinkInteractor {
    var deepLinkPath: String? = null

    fun getNavResourceFromDeepLink(): Int? {
        when(deepLinkPath) {
            "/carbon-android" -> {
                Timber.d("KAB TESTING - default deep link received")
                return null
            }
            "/carbon-android/path1" -> {
                Timber.d("KAB TESTING - path1 deep link received")
                return R.id.action_mainFragment_to_deepLinkPath1Fragment
            }
            "/carbon-android/path2" -> {
                Timber.d("KAB TESTING - path2 deep link received")
                return null
            }
            else -> {
                Timber.e("Deep link path not recognized")
                return null
            }
        }
    }
}