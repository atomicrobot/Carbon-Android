package com.atomicrobot.carbon.ui.designSystems

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.navigation.CarbonScreens
import com.atomicrobot.carbon.ui.components.CarbonTopBarNavigation
import com.atomicrobot.carbon.ui.components.DesignSystemTopBarActions
import com.atomicrobot.carbon.ui.navigation.AppBarState
import com.atomicrobot.carbon.ui.theme.*
import com.atomicrobot.carbon.util.LocalActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@Composable
fun DesignSystemScreen(
    designSystemViewModel: DesignSystemViewModel = hiltViewModel(LocalActivity.current),
    onDetailSelected: (String) -> Unit,
    onDrawerClicked: () -> Unit,
    onUpdateAppBarState: (AppBarState) -> Unit
) {

    val designSystemState by designSystemViewModel.designSystemState.collectAsState()

    LaunchedEffect(Unit) {
        onUpdateAppBarState(
            AppBarState(
                title = CarbonScreens.DesignSystems.title,
                navigation = {
                    CarbonTopBarNavigation(
                        onDrawerClicked = onDrawerClicked
                    )
                },
                actions = {
                    DesignSystemTopBarActions(
                        onDarkModeToggled = {
                            designSystemViewModel.applyAction(
                                DesignSystemViewModel.Event.ToggleDarkMode(
                                    it
                                )
                            )
                        },
                        onFontScaleSet = {
                            designSystemViewModel.applyAction(
                                DesignSystemViewModel.Event.SetFontScale(
                                    it
                                )
                            )
                        },
                        fontScale = designSystemState.fontScale,
                        darkMode = designSystemState.isInDarkMode
                    )
                }
            )
        )
    }

    CarbonAndroidTheme(
        darkTheme = designSystemState.isInDarkMode,
        testingFontScale = designSystemState.fontScale
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 2.dp)
            ) {
                item { Spacer(modifier = Modifier.height(16.dp)) }
                this.atoms(onDetailSelected)
                this.molecules(onDetailSelected)
                this.organisms(onDetailSelected)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
fun LazyListScope.atoms(
    onAtomClicked: (String) -> Unit
) {
    stickyHeader {
        DesignHomeListHeader(R.drawable.atom, stringResource(id = R.string.design_atoms))
    }
    items(Atom.values()) { atom ->
        DesignScreenRow(
            title = atom.category,
            onItemClicked = { onAtomClicked(atom.category) }
        )
    }
    item { Spacer(modifier = Modifier.height(16.dp)) }
}

@OptIn(ExperimentalFoundationApi::class)
fun LazyListScope.molecules(
    onMoleculeClicked: (String) -> Unit
) {
    stickyHeader {
        DesignHomeListHeader(R.drawable.molecule, stringResource(id = R.string.design_molecule))
    }
    items(Molecule.values()) { molecule ->
        DesignScreenRow(molecule.category) {
            onMoleculeClicked(molecule.category)
        }
    }
    item { Spacer(modifier = Modifier.height(16.dp)) }
}

@OptIn(ExperimentalFoundationApi::class)
fun LazyListScope.organisms(
    onOrganismClicked: (String) -> Unit
) {
    stickyHeader {
        DesignHomeListHeader(R.drawable.organism, stringResource(id = R.string.design_organisms))
    }
    items(Organism.values()) { organism ->
        DesignScreenRow(organism.category) {
            onOrganismClicked(organism.category)
        }
    }
}

@Composable
fun DesignHomeListHeader(
    iconRes: Int,
    title: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painterResource(id = iconRes),
                "Header section icon",
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
        Divider()
    }
}

@Composable
fun DesignScreenRow(
    title: String,
    onItemClicked: (String) -> Unit = {}
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable { onItemClicked(title) },
        color = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    ) {
        Row(
            modifier = Modifier
                .padding(start = 16.dp, top = 8.dp, end = 24.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                title,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(Icons.Filled.ChevronRight, "View $title")
        }
    }
}

@HiltViewModel
class DesignSystemViewModel @Inject constructor(
) : ViewModel() {

    private val _designSystemState = MutableStateFlow(State())

    val designSystemState: StateFlow<State> = _designSystemState.asStateFlow()

    fun applyAction(action: Event) {
        when (action) {
            is Event.ToggleDarkMode -> toggleDarkMode(action.darkMode)
            is Event.SetFontScale -> setFontScale(action.scale)
        }
    }

    private fun toggleDarkMode(darkMode: Boolean) {
        _designSystemState.update {
            it.copy(
                isInDarkMode = darkMode
            )
        }
    }

    private fun setFontScale(scale: Float) {
        _designSystemState.update {
            it.copy(
                fontScale = scale
            )
        }
    }

    data class State(
        val isInDarkMode: Boolean = false,
        val fontScale: Float = 1f
    )

    sealed class Event {
        class ToggleDarkMode(val darkMode: Boolean) : Event()
        class SetFontScale(val scale: Float) : Event()
    }
}

enum class Atom(val category: String) {
    COLORS(category = "Colors"),
    TYPOGRAPHY(category = "Typography"),
    FONTS(category = "Fonts"),
    SHAPES(category = "Shapes")
    //LOGOS(category = "Logos"),
    //ICONS(category = "Icons")
}

enum class Molecule(val category: String) {
    BUTTONS(category = "Buttons"),
    //CHECKBOXES(category = "Checkboxes"),
    //RADIOS(category = "Radios"),
    //SWITCHES(category = "Switches"),
    //SLIDERS(category = "Sliders"),
    TEXTFIELDS(category = "Text Fields"),
    BARS(category = "Bars"),
    PROGRESS(category = "Progress")
}

enum class Organism(val category: String) {
}