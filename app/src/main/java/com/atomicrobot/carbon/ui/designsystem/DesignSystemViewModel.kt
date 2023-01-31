package com.atomicrobot.carbon.ui.designsystem

import androidx.lifecycle.ViewModel
import com.atomicrobot.carbon.ui.designsystem.theme.FinAllyColors
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DesignSystemViewModel() : ViewModel() {
    data class ScreenState(
        val darkMode: Boolean = false,
        val useDynamicColor: Boolean = false,
        val fontScale: FontScale = FontScale.Normal,
    )

    private val _uiState = MutableStateFlow(ScreenState())
    val uiState: StateFlow<ScreenState>
        get() = _uiState

    val finAllySwatches: List<Pair<String, Int>> = FinAllyColors.colorMap.toList()

    fun enabledDarkMode(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(
            darkMode = enabled
        )
    }

    fun updateFontScale(fontScale: FontScale) {
        _uiState.value = _uiState.value.copy(
            fontScale = fontScale
        )
    }

    @Suppress("unused")
    fun enabledDynamicColor(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(
            useDynamicColor = enabled
        )
    }
}
