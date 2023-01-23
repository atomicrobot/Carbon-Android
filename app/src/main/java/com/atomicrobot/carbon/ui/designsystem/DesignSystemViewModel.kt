package com.atomicrobot.carbon.ui.designsystem

import android.app.Application
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DesignSystemViewModel(
    private val app: Application,
): ViewModel() {

    data class ScreenState(
        val darkMode: Boolean = false,
        val useDynamicColor: Boolean = false,
    )

    private val _uiState = MutableStateFlow(ScreenState())
    val uiState: StateFlow<ScreenState>
        get() = _uiState

    fun enabledDarkMode(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(
            darkMode = enabled
        )
    }

    fun enabledDynamicColor(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(
            useDynamicColor = enabled
        )
    }
}