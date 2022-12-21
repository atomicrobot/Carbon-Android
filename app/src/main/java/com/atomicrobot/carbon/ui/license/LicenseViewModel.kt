package com.atomicrobot.carbon.ui.license

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atomicrobot.carbon.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class LicenseViewModel @Inject constructor(
    private val app: Application
) : ViewModel() {
    sealed class Licenses {
        object Loading : Licenses()
        class Result(val licenses: String) : Licenses()
        class Error(val message: String) : Licenses()
    }

    data class LicenseScreenUiState(
        val licensesState: Licenses = Licenses.Result("")
    )

    private val _uiState = MutableStateFlow(LicenseScreenUiState())
    val uiState: StateFlow<LicenseScreenUiState>
        get() = _uiState

    fun getLicenses() {
        // Update the UI state to indicate that we are loading.
        _uiState.value = _uiState.value.copy(licensesState = Licenses.Loading)
        // Try to load licenses from markdown file
        viewModelScope.launch {
            try {
                kotlin.runCatching {
                    val inputStream: InputStream = app.assets.open("licenses.md")
                    val size: Int = inputStream.available()
                    val buffer = ByteArray(size)
                    inputStream.read(buffer)
                    String(buffer)
                }.onSuccess {
                    _uiState.value = _uiState.value.copy(licensesState = Licenses.Result(it))
                }.onFailure {
                    _uiState.value = _uiState.value.copy(
                        licensesState = Licenses.Error(
                            app.getString(R.string.error_unexpected)
                        )
                    )
                }
            } catch (error: Exception) {
                Timber.e(error)
                _uiState.value = _uiState.value.copy(
                    licensesState = Licenses.Error(
                        error.message ?: app.getString(R.string.error_unexpected)
                    )
                )
            }
        }
    }
}
