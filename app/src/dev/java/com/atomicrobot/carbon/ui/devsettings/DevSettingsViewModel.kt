package com.atomicrobot.carbon.ui.devsettings

import android.app.Application
import com.atomicrobot.carbon.app.Settings
import com.atomicrobot.carbon.ui.BaseViewModel

class DevSettingsViewModel(
    private val app: Application,
    private val settings: Settings
) :
    BaseViewModel(app) {

    override fun setupViewModel() {
        baseUrl = settings.baseUrl
        trustAllSSL = settings.trustAllSSL
    }

    var baseUrl: String = ""

    var trustAllSSL: Boolean = false

    fun saveSettings() {
        settings.baseUrl = baseUrl
        settings.trustAllSSL = trustAllSSL
    }

    companion object {
        private const val STATE_KEY = "DevSettingsViewModelState" // NON-NLS
    }
}
