package com.atomicrobot.carbon.ui.designSystems

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.atomicrobot.carbon.ui.designSystems.DesignSystemScreenState.*
import com.atomicrobot.carbon.ui.designSystems.DesignSystemViewModel.Event
import com.atomicrobot.carbon.ui.theme.CarbonAndroidTheme
import com.atomicrobot.carbon.ui.theme.DefaultDarkColorPalette
import com.atomicrobot.carbon.ui.theme.DefaultLightColorPalette
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DesignSystemScreen(
    designSystemViewModel: DesignSystemViewModel,
    onDismiss: () -> Unit
) {

    var expandedMenu by remember { mutableStateOf(false) }

    var screenState: DesignSystemScreenState by remember { mutableStateOf(OverviewState) }

    val designSystemState by designSystemViewModel.designSystemState.collectAsState()

    BackHandler(
        enabled = true,
        onBack = {
            when (screenState) {
                is DetailState -> screenState = OverviewState
                is OverviewState -> onDismiss()
            }
        }
    )

    CarbonAndroidTheme(
        darkTheme = designSystemState.isInDarkMode,
        testingFontScale = designSystemState.fontScale
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            AdjustmentsRow(
                expandedMenu = expandedMenu,
                darkModeChecked = designSystemState.isInDarkMode,
                textScale = designSystemState.fontScale,
                screenState = screenState,
                onMenuExpandedStateChanged = {
                    expandedMenu = it
                },
                onDarkModeChanged = {
                    designSystemViewModel.applyAction(Event.ToggleDarkMode(it))
                },
                onTextScaleChanged = {
                    designSystemViewModel.applyAction(Event.SetFontScale(it))
                    expandedMenu = false
                },
                onReturnFromDetail = {
                    screenState = OverviewState
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            AnimatedContent(
                targetState = screenState,
                transitionSpec = {
                    if (targetState is DetailState) {
                        slideInHorizontally(
                            animationSpec = tween(),
                            initialOffsetX = { width -> width }
                        ) with
                                slideOutHorizontally(
                                    animationSpec = tween(),
                                    targetOffsetX = { width -> -width }
                                )
                    } else {
                        slideInHorizontally(
                            animationSpec = tween(),
                            initialOffsetX = { width -> -width }
                        ) with
                                slideOutHorizontally(
                                    animationSpec = tween(),
                                    targetOffsetX = { width -> width }
                                )
                    }
                }
            ) {
                when (screenState) {
                    is OverviewState -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            this.atomsGroup(
                                onAtomClicked = {
                                    screenState = DetailState(it.category)
                                }
                            )

                            item {
                                Spacer(modifier = Modifier.height(8.dp))
                                Divider()
                                Spacer(modifier = Modifier.height(8.dp))
                            }

                            this.moleculesGroup(
                                onMoleculeClicked = {
                                    screenState = DetailState(it.category)
                                }
                            )
                        }
                    }
                    is DetailState -> {

                        val composables = selectedComposableSet(screenState.category)

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {

                            items(composables) { composable ->
                                composable()
                            }
                        }
                    }
                }
            }
        }
    }
}

fun LazyListScope.atomsGroup(
    onAtomClicked: (Atom) -> Unit
) {
    items(Atom.values()) { atom ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onAtomClicked(atom)
                }
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                text = atom.category
            )
        }
    }
}

fun LazyListScope.moleculesGroup(
    onMoleculeClicked: (Molecule) -> Unit
) {
    items(Molecule.values()) { molecule ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onMoleculeClicked(molecule)
                }
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                text = molecule.category
            )
        }
    }
}

@Composable
fun AdjustmentsRow(
    expandedMenu: Boolean,
    darkModeChecked: Boolean,
    textScale: Float,
    screenState: DesignSystemScreenState,
    onMenuExpandedStateChanged: (Boolean) -> Unit,
    onDarkModeChanged: (Boolean) -> Unit,
    onTextScaleChanged: (Float) -> Unit,
    onReturnFromDetail: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (screenState is DetailState) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onReturnFromDetail
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Return to Overview"
                    )
                }
            }
        }

        Box(modifier = Modifier.wrapContentSize(Alignment.TopStart)) {
            OutlinedButton(
                onClick = { onMenuExpandedStateChanged(!expandedMenu) },
                shape = RectangleShape,
                contentPadding = PaddingValues(
                    start = 12.dp,
                    top = 8.dp,
                    bottom = 8.dp,
                    end = 0.dp
                ),
                content = {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${textScale}x Text Scale",
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = "dropdown menu",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            )

            DropdownMenu(
                modifier = Modifier.background(color = MaterialTheme.colorScheme.onSecondaryContainer),
                expanded = expandedMenu,
                onDismissRequest = { onMenuExpandedStateChanged(false) }
            ) {
                DropdownMenuItem(
                    text = {
                        Text(text = "0.75x")
                    },
                    colors = MenuDefaults.itemColors(
                        textColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    onClick = {
                        onTextScaleChanged(0.75f)
                    }
                )
                DropdownMenuItem(
                    text = {
                        Text(text = "1.0x")
                    },
                    colors = MenuDefaults.itemColors(
                        textColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    onClick = {
                        onTextScaleChanged(1.0f)
                    }
                )
                DropdownMenuItem(
                    text = {
                        Text(text = "1.5x")
                    },
                    colors = MenuDefaults.itemColors(
                        textColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    onClick = {
                        onTextScaleChanged(1.5f)
                    }
                )
                DropdownMenuItem(
                    text = {
                        Text(text = "2.0x")
                    },
                    colors = MenuDefaults.itemColors(
                        textColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    onClick = {
                        onTextScaleChanged(2.0f)
                    }
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Switch(
            checked = darkModeChecked,
            onCheckedChange = onDarkModeChanged
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "Dark Mode",
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
fun selectedComposableSet(
    category: String?,
    definedColors: Set<String> = emptySet()
): List<@Composable () -> Unit> {

    return when (category){
        Atom.COLORS.category -> getColorSchemeComposables(definedColors)
        Atom.TYPOGRAPHY.category -> getTypographyComposables()
        Atom.FONTS.category -> getFontComposables()
        Molecule.BUTTONS.category -> getButtonComposables()
        else -> listOf()
    }
}

@HiltViewModel
class DesignSystemViewModel @Inject constructor(
) : ViewModel() {

    private val _designSystemState = MutableStateFlow(State())

    val designSystemState: StateFlow<State> = _designSystemState.asStateFlow()

    fun applyAction(action: Event){
        when (action){
            is Event.ToggleDarkMode -> toggleDarkMode(action.darkMode)
            is Event.SetFontScale -> setFontScale(action.scale)
            is Event.AlterColor -> alterColor(action.colorPair)
        }
    }

    private fun toggleDarkMode(darkMode: Boolean){
        _designSystemState.update {
            it.copy(
                isInDarkMode = darkMode
            )
        }
    }

    private fun setFontScale(scale: Float){
        _designSystemState.update {
            it.copy(
                fontScale = scale
            )
        }
    }

    private fun alterColor(pair: Pair<String,Color>){

    }

    data class State(
        val isInDarkMode: Boolean = false,
        val fontScale: Float = 1f,
        val definedColors: Set<String> = if (isInDarkMode) DefaultDarkColorPalette.definedColorMap.keys else DefaultLightColorPalette.definedColorMap.keys
    )

    sealed class Event {
        class ToggleDarkMode(val darkMode: Boolean) : Event()
        class SetFontScale(val scale: Float) : Event()
        class AlterColor(val colorPair: Pair<String,Color>) : Event()
    }
}

enum class Atom(val category: String) {
    COLORS(category = "Colors"),
    TYPOGRAPHY(category = "Typography"),
    FONTS(category = "Fonts"),
    LOGOS(category = "Logos"),
    ICONS(category = "Icons")
}

enum class Molecule(val category: String) {
    BUTTONS(category = "Buttons"),
    CHECKBOXES(category = "Checkboxes"),
    RADIOS(category = "Radios"),
    SWITCHES(category = "Switches"),
    SLIDERS(category = "Sliders"),
    PICKERS(category = "Pickers"),
    TEXTFIELDS(category = "Text Fields"),
    BARS(category = "Bars")
}

sealed class DesignSystemScreenState(val category: String? = null) {
    object OverviewState : DesignSystemScreenState()
    class DetailState(detail: String) : DesignSystemScreenState(detail)
}