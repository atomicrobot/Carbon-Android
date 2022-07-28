package com.atomicrobot.carbon.ui.permissions

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistryOwner
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.app.ActivityOptionsCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@HiltViewModel
class NotificationsPermissionsViewModel @Inject constructor(
    private val app: Application
) : BaseViewModel(app) {


    sealed class UiState {
        object Unknown : UiState()
        object Initial : UiState()
        object Denied : UiState()
        object Granted: UiState()
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val _uiState = MutableLiveData<UiState>(UiState.Unknown)
    val uiState: LiveData<UiState>
        get() = _uiState

    fun setState(state: UiState) {
        _uiState.value = state
    }

    override fun setupViewModel() {

    }


    fun notificationsPermissionsGranted(): Boolean {
        return uiState.value != UiState.Denied && uiState.value != UiState.Initial
    }



//    fun configureState(activity: AppCompatActivity) {
//        Timber.tag("NotificationsPermissionsViewModel").d("doStartup()")
//
//        if ( uiState.value == UiState.Unknown) {
//            if (NotificationManagerCompat.from(app).areNotificationsEnabled()) {
//                _uiState.value = UiState.Granted
//                return
//            }
//
//            var shouldShowNotificationsNotEnabled = true
//
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
//                shouldShowNotificationsNotEnabled = activity.shouldShowRequestPermissionRationale(
//                    Manifest.permission.POST_NOTIFICATIONS
//                )
//            }
//
//            if (shouldShowNotificationsNotEnabled) {
//                _uiState.value = UiState.Denied
//            } else {
//                _uiState.value = UiState.Initial
//            }
//        }
//    }
//
//    fun askForPermission(activity: AppCompatActivity): Boolean {
//        configureState(activity)
//        Timber.tag("NotificationsPermissionsViewModel").d("Granted: "+(uiState.value != UiState.Granted)+" UiStateUIState: "+uiState.value)
//        return (uiState.value != UiState.Granted)
//    }

    fun getDescription(): String {
        when ( uiState.value ) {
            UiState.Initial -> {
                return app.getString(R.string.notifications_pre_ask_description)
            } else -> {
                return app.getString(R.string.notifications_not_enabled_description)
            }
        }
    }

    fun getButtonLabel(): String {
        when ( uiState.value ) {
            UiState.Initial -> {
                return app.getString(R.string.notifications_pre_ask_allow_notifications)
            } else -> {
                return app.getString(R.string.notifications_update_settings)
            }
        }
    }

    fun getLaterButtonLabel(): String {
        return app.getString(R.string.notifications_pre_ask_maybe_later)
    }

}



