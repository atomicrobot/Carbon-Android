package com.mycompany.myapp.ui.devsettings

import android.databinding.Bindable
import android.os.Parcelable
import com.mycompany.myapp.BR
import com.mycompany.myapp.app.Settings
import com.mycompany.myapp.databinding.ReadWriteBinding
import com.mycompany.myapp.ui.BasePresenter
import com.mycompany.myapp.ui.devsettings.DevSettingsPresenter.DevSettingsViewContract
import kotlinx.android.parcel.Parcelize

class DevSettingsPresenter(
        private val settings: Settings)
    : BasePresenter<DevSettingsViewContract, DevSettingsPresenter.State>(STATE_KEY, State()) {

    interface DevSettingsViewContract

    @Parcelize
    data class State(
            var initialized: Boolean = false,
            var baseUrl: String = "",
            var trustAllSSL: Boolean = false) : Parcelable

    override fun onResume() {
        super.onResume()
        if (!state.initialized) {
            state.initialized = true
            baseUrl = settings.baseUrl
            isTrustAllSSL = settings.trustAllSSL
        }
    }

    @get:Bindable var baseUrl: String by ReadWriteBinding(BR.baseUrl) { state::baseUrl }
    @get:Bindable var isTrustAllSSL: Boolean by ReadWriteBinding(BR.trustAllSSL) { state::trustAllSSL }

    fun saveSettingsAndRestart() {
        settings.baseUrl = state.baseUrl
        settings.trustAllSSL = state.trustAllSSL
    }

    companion object {
        private const val STATE_KEY = "DevSettingsPresenterState"  // NON-NLS
    }
}
