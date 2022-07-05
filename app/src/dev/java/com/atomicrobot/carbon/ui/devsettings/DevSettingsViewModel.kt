package com.atomicrobot.carbon.ui.devsettings

import android.app.Application
import android.os.Parcelable
import androidx.databinding.Bindable
import com.atomicrobot.carbon.BR
import com.atomicrobot.carbon.app.Settings
import com.atomicrobot.carbon.ui.BaseViewModel
import kotlinx.parcelize.Parcelize

class DevSettingsViewModel(
        private val app: Application,
        private val settings: Settings)
    : BaseViewModel(app) {

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
        private const val STATE_KEY = "DevSettingsViewModelState"  // NON-NLS
    }
}
