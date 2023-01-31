package com.atomicrobot.carbon.ui.designsystem

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.atomicrobot.carbon.ui.designsystem.theme.FinAllyColors
import com.atomicrobot.carbon.util.splitCamelCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*
import kotlin.reflect.full.memberProperties

class DesignSystemViewModel() : ViewModel() {
    data class ScreenState(
        val darkMode: Boolean = false,
        val useDynamicColor: Boolean = false,
        val fontScale: FontScale = FontScale.Normal,
    )

    private val _uiState = MutableStateFlow(ScreenState())
    val uiState: StateFlow<ScreenState>
        get() = _uiState

    val finAllySwatches: List<Pair<String, Color>> = FinAllyColors::class.memberProperties.map {
        val colorName = splitCamelCase(it.name).replaceFirstChar { firstChar ->
            if (firstChar.isLowerCase()) firstChar.titlecase(Locale.ROOT) else it.toString()
        }
        val color = it.get(FinAllyColors::class.objectInstance!!) as Color
        Pair(colorName, color)
    }.toList()

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
